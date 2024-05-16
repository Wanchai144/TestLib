package com.tdg.vroom.data.model.response


import com.google.gson.annotations.SerializedName

data class ModelPackage(
    @SerializedName("token")
    val token:String?,
    @SerializedName("acr")
    val acr: String?,
    @SerializedName("aud")
    val aud: String?,
    @SerializedName("auth_time")
    val authTime: Int?,
    @SerializedName("azp")
    val azp: String?,
    @SerializedName("context")
    val context: Context?,
    @SerializedName("exp")
    val exp: Int?,
    @SerializedName("iat")
    val iat: Int?,
    @SerializedName("iss")
    val iss: String?,
    @SerializedName("jti")
    val jti: String?,
    @SerializedName("preferred_username")
    val preferredUsername: String?,
    @SerializedName("room")
    val room: String?,
    @SerializedName("session_state")
    val sessionState: String?,
    @SerializedName("sub")
    val sub: String?,
    @SerializedName("typ")
    val typ: String?
){
    data class User(
        @SerializedName("avatar")
        val avatar: String?,
        @SerializedName("email")
        val email: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("iss")
        val iss: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("p")
        val p: String?,
        @SerializedName("pkgcode")
        val pkgcode: String?,
        @SerializedName("restrict_name")
        val restrictName: String?
    )

    data class Features(
        @SerializedName("background")
        val background: Boolean?,
        @SerializedName("mom")
        val mom: Boolean?,
        @SerializedName("participant-count")
        val participantCount: Boolean?,
        @SerializedName("participantlist")
        val participantlist: Boolean?,
        @SerializedName("screen-sharing")
        val screenSharing: Boolean?,
        @SerializedName("shutup")
        val shutup: Boolean?,
        @SerializedName("vote")
        val vote: Boolean?,
        @SerializedName("whiteboard")
        val whiteboard: Boolean?
    )

    data class Context(
        @SerializedName("features")
        val features: Features?,
        @SerializedName("group")
        val group: String?,
        @SerializedName("user")
        val user: User?
    )
}

data class PackageName(
    @SerializedName("displayName")
    val displayName: String?,
    @SerializedName("i")
    val i: I?,
    @SerializedName("mp")
    val mp: Int?,
    @SerializedName("packageCode")
    val packageCode: String?,
    @SerializedName("packageNameEN")
    val packageNameEN: String?,
    @SerializedName("r")
    val r: R?
){
    data class R(
        @SerializedName("d")
        val d: Int?
    )

    data class I(
        @SerializedName("d")
        val d: Int?
    )
}