package com.tdg.vroom.data.remote.api

import com.tdg.vroom.data.model.response.CheckVersionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface VersionMeetingApiService {

    @GET("api/v1/api/version/{roomName}")
    suspend fun checkVersionMeeting(
        @Path("roomName") roomName: String
    ): Response<CheckVersionResponse>
}