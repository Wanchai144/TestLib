package com.example.testlibs.data.repository

import com.example.testlibs.data.LiveKitGenerateTokenResponse
import com.example.testlibs.data.service.LiveKitApiService
import retrofit2.Response

interface GeneralRepository {
    suspend fun doGenerateTokenLiveKit(
        identity: String, name: String, roomName: String
    ): Response<LiveKitGenerateTokenResponse>?
}

class GeneralRepositoryImpl (
    private val liveKitApiService: LiveKitApiService,
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
}
