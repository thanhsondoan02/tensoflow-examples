package org.tensorflow.lite.examples.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.car.tpms.AudioEncoding
import com.car.tpms.RecognitionConfig
import com.car.tpms.RivaSpeechRecognitionGrpc
import com.car.tpms.RivaSpeechSynthesisGrpc
import com.car.tpms.StreamingRecognitionConfig
import com.car.tpms.StreamingRecognizeRequest
import com.car.tpms.StreamingRecognizeResponse
import com.car.tpms.SynthesizeSpeechRequest
import com.car.tpms.SynthesizeSpeechResponse
import com.google.protobuf.ByteString
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.TlsChannelCredentials
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.tensorflow.lite.examples.audio.api.APIService
import org.tensorflow.lite.examples.audio.model.TimiRequest
import org.tensorflow.lite.examples.audio.model.TimiResponse
import org.tensorflow.lite.examples.audio.model.TtsModel
import org.tensorflow.lite.examples.audio.model.VaModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.lang.RuntimeException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity2 : AppCompatActivity() {
    companion object {
        const val CHANNEL_IP = "167.179.48.117"
        const val CHANNEL_PORT = 50051
        const val SAMPLING_RATE = 16000
        const val LANGUAGE_CODE = "vi-VN"
        const val ENABLE_AUTOMATIC_PUNCTUATION = true
        const val STREAM_INTERIM_RESULTS = true
    }

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val btnSave = findViewById<Button>(R.id.btnMain2)
        val edtType = findViewById<EditText>(R.id.edtMain2)


        btnSave.setOnClickListener {
            start()
        }
    }

    private fun start() {
        val tvMessage = findViewById<TextView>(R.id.tvMain2)
        val audioRecorder = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
            return
        } else {
            AudioRecord(
                1,
                SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioRecord.getMinBufferSize(
                    SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )
            )

        }

        audioRecorder.startRecording()

        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLING_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val buffer = ByteArray(bufferSize)

        val creds =
            TlsChannelCredentials.newBuilder().trustManager(assets.open("ca-cert.pem")).build()
        val channel = Grpc.newChannelBuilder("$CHANNEL_IP:$CHANNEL_PORT", creds).build()

        val nonBlockingStub = RivaSpeechRecognitionGrpc.newStub(channel)
        val streamingConfig = StreamingRecognitionConfig.newBuilder()
            .setConfig(
                RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR_PCM)
                    .setSampleRateHertz(SAMPLING_RATE)
                    .setLanguageCode(LANGUAGE_CODE)
                    .setMaxAlternatives(1)
                    .setEnableAutomaticPunctuation(ENABLE_AUTOMATIC_PUNCTUATION)
                    .setVerbatimTranscripts(true)
                    .build()
            )
            .setInterimResults(STREAM_INTERIM_RESULTS)
            .build()

        val requestObserver = nonBlockingStub.streamingRecognize(object :
            StreamObserver<StreamingRecognizeResponse> {
            override fun onNext(value: StreamingRecognizeResponse?) {
                if (value?.resultsList?.firstOrNull()?.isFinal == true) {
                    value.resultsList?.firstOrNull()?.alternativesList?.firstOrNull()?.transcript?.let {
                        Log.e("vlalalvalv", it)
                        audioRecorder.stop()
                        onCompleted()
                        channel.shutdownNow()

                        CoroutineScope(Dispatchers.IO).launch {
                            APIService.base().answer(
                                TimiRequest(
                                    TtsModel(1, 0.5f, 1, "huyen_nguyen"),
                                    VaModel("default", it)
                                )
                            ).enqueue(object : Callback<TimiResponse> {
                                override fun onResponse(
                                    call: Call<TimiResponse>,
                                    response: Response<TimiResponse>
                                ) {
                                    runOnUiThread {
                                        tvMessage.text = response.body()?.data?.text
                                    }
                                    textToSpeech(it)
                                    start()
                                }

                                override fun onFailure(call: Call<TimiResponse>, t: Throwable) {
                                    Log.e("vlalalvalv", "onFailure ${t.message}")
                                }
                            })
                        }
                    }
                } else {
                    val alternativeList = value?.resultsList?.firstOrNull()?.alternativesList
                    alternativeList?.let {
                        for (v in it) {
                            Log.e("vlalalvalv", v.transcript)
                        }
                    }
                }
            }

            override fun onError(t: Throwable?) {
                Log.e("vlalalvalv", "onError ${t?.message}")
            }

            override fun onCompleted() {
                Log.e("vlalalvalv", "onCompleted")
            }
        })

        startStream(audioRecorder, buffer, bufferSize, requestObserver!!, streamingConfig)
    }

    private fun textToSpeech(text: String) {
        val creds =
            TlsChannelCredentials.newBuilder().trustManager(assets.open("ca-cert.pem")).build()
        val channel = Grpc.newChannelBuilder("$CHANNEL_IP:$CHANNEL_PORT", creds).build()
        val stub = RivaSpeechSynthesisGrpc.newStub(channel)
        val request = SynthesizeSpeechRequest.newBuilder()
            .setLanguageCode(LANGUAGE_CODE)
            .setEncoding(AudioEncoding.LINEAR_PCM)
            .setText(text)
            .build()
        stub.synthesizeOnline(request, object : StreamObserver<SynthesizeSpeechResponse> {
            override fun onNext(value: SynthesizeSpeechResponse?) {
                val byteString = value?.audio
                val audioData = byteString?.toByteArray()
                val audioTrack = AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    SAMPLING_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    audioData!!.size,
                    AudioTrack.MODE_STATIC
                )
                audioTrack.write(audioData, 0, audioData.size)
                audioTrack.play()
            }

            override fun onError(t: Throwable?) {
                Log.e("vlalalvalv", "TTS onError ${t?.message}")
            }

            override fun onCompleted() {
                Log.e("vlalalvalv", "TTS onCompleted")
            }
        })
    }

    private fun startStream(
        audioRecorder: AudioRecord,
        buffer: ByteArray,
        bufferSize: Int,
        requestObserver: StreamObserver<StreamingRecognizeRequest>,
        streamingConfig: StreamingRecognitionConfig
    ) {
        val request =
            StreamingRecognizeRequest.newBuilder().setStreamingConfig(streamingConfig).build()
        requestObserver.onNext(request)

        CoroutineScope(Dispatchers.IO).launch {
            while (audioRecorder.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecorder.read(buffer, 0, bufferSize)
                val rq = StreamingRecognizeRequest.newBuilder()
                    .setAudioContent(ByteString.copyFrom(buffer))
                    .build()
                requestObserver.onNext(rq)
            }
        }
    }
}
