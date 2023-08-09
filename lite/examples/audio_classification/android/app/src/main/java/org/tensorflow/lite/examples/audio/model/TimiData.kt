package org.tensorflow.lite.examples.audio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimiData(
    @SerializedName("text")
    @Expose
    var text: String?,

    @SerializedName("audio_link")
    @Expose
    val audioLink: String?,
)
