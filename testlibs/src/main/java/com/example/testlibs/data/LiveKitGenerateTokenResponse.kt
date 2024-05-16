package com.example.testlibs.data

import com.google.gson.annotations.SerializedName

data class LiveKitGenerateTokenResponse(
    @SerializedName("identity")
    val identity: String? = "",
    @SerializedName("accessToken")
    val accessToken: String? = ""
)

