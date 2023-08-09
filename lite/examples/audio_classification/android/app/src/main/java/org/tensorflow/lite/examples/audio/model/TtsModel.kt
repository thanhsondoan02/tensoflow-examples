package org.tensorflow.lite.examples.audio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TtsModel(
    @SerializedName("alpha")
    @Expose
    val alpha: Int?,

    @SerializedName("dot_time")
    @Expose
    val dotTime: Float?,

    @SerializedName("para_time")
    @Expose
    val paraTime: Int?,

    @SerializedName("speaker")
    @Expose
    val speaker: String?
)
