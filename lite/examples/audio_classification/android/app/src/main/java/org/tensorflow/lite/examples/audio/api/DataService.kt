package org.tensorflow.lite.examples.audio.api

import org.tensorflow.lite.examples.audio.model.TimiRequest
import org.tensorflow.lite.examples.audio.model.TimiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DataService {

    @POST("asr")
    fun answer(@Body request: TimiRequest): Call<TimiResponse>
}
