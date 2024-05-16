package com.tdg.vroom.data.model.response
import com.google.gson.annotations.SerializedName


data class CheckVersionResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("status")
    val status: Status?
)

class Data

data class Status(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?
)