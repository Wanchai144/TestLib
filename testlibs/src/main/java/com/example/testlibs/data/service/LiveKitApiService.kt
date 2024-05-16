package com.example.testlibs.data.service

import com.example.testlibs.data.LiveKitGenerateTokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LiveKitApiService {

    @GET("token")
    suspend fun doGenerateToken(
        @Query("identity") identity: String,
        @Query("name") name: String,
        @Query("roomName") roomName: String
    ): Response<LiveKitGenerateTokenResponse>
}