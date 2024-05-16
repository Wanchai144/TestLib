package com.tdg.vroom.data.model.request

import com.google.gson.annotations.SerializedName
import com.tdg.vroom.BuildConfig

data class LoginBody(
    @SerializedName("grant_type")
    val grantType: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("client_id")
    val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.CLIENT_SECERT,
)
