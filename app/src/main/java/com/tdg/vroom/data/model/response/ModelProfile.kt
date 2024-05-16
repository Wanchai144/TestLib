package com.tdg.vroom.data.model.response


import com.google.gson.annotations.SerializedName

data class ModelProfile(
    @SerializedName("at_hash")
    val atHash: String?,
    @SerializedName("aud")
    val aud: String?,
    @SerializedName("auth_time")
    val authTime: Int?,
    @SerializedName("azp")
    val azp: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verified")
    val emailVerified: Boolean?,
    @SerializedName("exp")
    val exp: Int?,
    @SerializedName("family_name")
    val familyName: String?,
    @SerializedName("given_name")
    val givenName: String?,
    @SerializedName("iat")
    val iat: Int?,
    @SerializedName("iss")
    val iss: String?,
    @SerializedName("jti")
    val jti: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("preferred_username")
    val preferredUsername: String?,
    @SerializedName("session_state")
    val sessionState: String?,
    @SerializedName("sid")
    val sid: String?,
    @SerializedName("sub")
    val sub: String?,
    @SerializedName("typ")
    val typ: String?
)