package org.tensorflow.lite.examples.audio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimiRequest(
    @SerializedName("tts_model")
    @Expose
    val ttsModel: TtsModel?,

    @SerializedName("va_model")
    @Expose
    val vaModel: VaModel?
)
