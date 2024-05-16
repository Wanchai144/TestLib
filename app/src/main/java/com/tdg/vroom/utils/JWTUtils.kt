package com.tdg.vroom.utils

import android.annotation.SuppressLint
import android.util.Base64
import com.google.gson.Gson
import java.io.UnsupportedEncodingException


object JWTUtils {
    @SuppressLint("LogNotTimber")
    @Throws(Exception::class)
    inline fun <reified T> decoded(jwtEncoded: String): T? {
        return try {
            val split =
                jwtEncoded.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            MyLog.d("Header: " + getJson(split[0]))
            MyLog.d("Body: " + getJson(split[1]))
            deseRializeObject(decodedJwt = getJson(split[1]))
        } catch (e: UnsupportedEncodingException) {
            null
        }
    }

    @SuppressLint("LogNotTimber")
    @Throws(Exception::class)
    inline fun <reified T> decodedPackage(jwtEncoded: String): T? {
        val decodePackageName = Base64.decode(jwtEncoded, Base64.DEFAULT).decodeToString()
        MyLog.d("Body: $decodePackageName")
        return deseRializeObject(decodedJwt = decodePackageName)
    }

    inline fun <reified T> deseRializeObject(decodedJwt: String?): T {
        val mGson = Gson()
        return mGson.fromJson(decodedJwt, T::class.java)
    }

    @Throws(UnsupportedEncodingException::class)
    fun getJson(strEncoded: String): String {
        val decodedBytes: ByteArray = Base64.decode(strEncoded, Base64.URL_SAFE)
        return String(decodedBytes, charset("UTF-8"))
    }
}