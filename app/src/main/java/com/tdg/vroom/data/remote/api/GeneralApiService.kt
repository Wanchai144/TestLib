package com.tdg.vroom.data.remote.api

import com.tdg.vroom.data.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GeneralApiService {

    @FormUrlEncoded
    @POST("auth/realms/vroom/protocol/openid-connect/token")
    suspend fun doLogin(
        @Field("grant_type") grant_type: String,
        @Field("scope") scope: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String
    ): Response<LoginResponse>
}