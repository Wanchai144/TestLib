package com.tdg.vroom.data.repository

import com.tdg.vroom.BuildConfig
import com.tdg.vroom.data.model.request.LoginBody
import com.tdg.vroom.data.model.response.CheckVersionResponse
import com.tdg.vroom.data.model.response.LiveKitGenerateTokenResponse
import com.tdg.vroom.data.model.response.LoginResponse
import com.tdg.vroom.data.model.response.ModelPackage
import com.tdg.vroom.data.remote.api.GeneralApiService
import com.tdg.vroom.data.remote.api.LiveKitApiService
import com.tdg.vroom.data.remote.api.PackageApiService
import com.tdg.vroom.data.remote.api.VersionMeetingApiService
import retrofit2.Response
import javax.inject.Inject

interface GeneralRepository {
    suspend fun doGenerateTokenLiveKit(
        identity: String, name: String, roomName: String
    ): Response<LiveKitGenerateTokenResponse>?

    suspend fun onLogin(loginBody: LoginBody): Response<LoginResponse>
    suspend fun getPackageName(token: String): Response<ModelPackage>
    suspend fun checkVersionMeeting(roomName: String): Response<CheckVersionResponse>
}

class GeneralRepositoryImpl @Inject constructor(
    private val liveKitApiService: LiveKitApiService,
    private val generalApiService: GeneralApiService,
    private val packageApiService: PackageApiService,
    private val versionMeetingApiService: VersionMeetingApiService
) : GeneralRepository {

    override suspend fun doGenerateTokenLiveKit(
        identity: String,
        name: String,
        roomName: String
    ): Response<LiveKitGenerateTokenResponse> {
        return liveKitApiService.doGenerateToken(
            identity = identity,
            name = name,
            roomName = roomName
        )
    }

    override suspend fun onLogin(loginBody: LoginBody): Response<LoginResponse> {
        return generalApiService.doLogin(
            grant_type = loginBody.grantType,
            scope = loginBody.scope,
            username = loginBody.username,
            password = loginBody.password,
            client_id = BuildConfig.CLIENT_ID,
            client_secret = BuildConfig.CLIENT_SECERT
        )
    }

    override suspend fun getPackageName(token: String): Response<ModelPackage> {
        return packageApiService.getPackageName(auth = "Bearer $token")
    }

    override suspend fun checkVersionMeeting(roomName: String): Response<CheckVersionResponse> {
        return versionMeetingApiService.checkVersionMeeting(roomName = roomName)
    }
}
