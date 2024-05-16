package com.tdg.vroom.data.model.response

import com.google.gson.annotations.SerializedName

data class LiveKitGenerateTokenResponse(
    @SerializedName("identity")
    val identity: String? = "",
    @SerializedName("accessToken")
    val accessToken: String? = ""
)
