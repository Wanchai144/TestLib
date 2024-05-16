package com.tdg.vroom.data.remote.api

import com.tdg.vroom.data.model.response.ModelPackage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface PackageApiService {

    @GET("v1/token")
    suspend fun getPackageName(
        @Header("Authorization") auth: String,
    ): Response<ModelPackage>
}