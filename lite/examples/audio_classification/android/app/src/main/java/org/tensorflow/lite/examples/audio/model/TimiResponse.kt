package org.tensorflow.lite.examples.audio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimiResponse(
    @SerializedName("code")
    @Expose
    val code: Int?,

    @SerializedName("data")
    @Expose
    val data: TimiData?,

    @SerializedName("msg")
    @Expose
    val msg: String?,
)
