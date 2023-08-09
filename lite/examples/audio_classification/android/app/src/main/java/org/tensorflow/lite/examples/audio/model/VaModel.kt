package org.tensorflow.lite.examples.audio.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VaModel(
    @SerializedName("sender_id")
    @Expose
    val senderId: String?,

    @SerializedName("text")
    @Expose
    val text: String?
)
