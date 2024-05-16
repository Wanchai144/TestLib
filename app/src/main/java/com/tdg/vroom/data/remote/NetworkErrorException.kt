package com.tdg.vroom.data.remote

import com.google.gson.Gson
import com.tdg.vroom.data.model.response.ModelError
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

object NetworkErrorException {

    fun <T> onErrorException(
        request: Response<T>,
        onSuccess: (Boolean) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if (request.isSuccessful) {
                onSuccess.invoke(true)
            } else {
                val error = request.errorBody()!!.charStream().readText()
                onError.invoke(deseRializeObject(error))
            }
        } catch (e: HttpException) {
            when (e.code()) {
                502 -> {
                    onError.invoke("internal error!")
                }

                403 -> {
                    onError.invoke("403")
                }

                401 -> {
                    onError.invoke("Auth expire")
                }

                400 -> {
                    onError.invoke("Something else went wrong")
                }
            }
        } catch (e: SocketTimeoutException) {
            onError.invoke("Connect timeout")
        } catch (e: Throwable) {
            onError.invoke("Something went wrong")
        }
    }

    private fun deseRializeObject(errorString: String?): String {
        val mGson = Gson()
        return try {
            return mGson.fromJson(errorString, ModelError::class.java).errorDescription ?: ""
        } catch (e: Exception) {
            e.message.toString()
        }
    }
}