package com.tdg.vroom.data.local.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.tdg.vroom.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferenceUtility @Inject constructor(@ApplicationContext val context: Context) :
    SharePreferenceUtility {
    companion object {
        const val LANGUAGE_KEY = "language"
        const val FULL_NAME = "fullName"
        const val EMAIL = "email"
        const val PROFILE_AVATAR = "avatar"
        const val ACCEPT_TOKEN = "acceptToken"
        const val ID_TOKEN = "idToken"
        const val PACKAGE_NAME = "packageName"
        const val PACKAGE_CODE = "packageCode"
        const val FLAG_LOGIN = "stateLogin"
        const val SWITCH_AUDIO_MUTED = "audioMuted"
        const val SWITCH_VIDEO_MUTED = "audioVideo"
        const val SWITCH_BATTERY_SAVING = "batterySaving"
    }

    private val fileName = "${context.resources.getString(R.string.app_name)}_prefs"

    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        fileName,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun putLong(key: String, value: Long) {
    }

    override fun putInt(key: String, value: Int) {
        encryptedSharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

    override fun getLong(key: String, default: Long): Long =
        encryptedSharedPreferences.getLong(key, default)

    override fun getInt(key: String, default: Int): Int =
        encryptedSharedPreferences.getInt(key, default)

    override fun putBoolean(key: String, value: Boolean) {
        encryptedSharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun getString(key: String, defaultValue: String): String {
        return encryptedSharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        val editor = encryptedSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return encryptedSharedPreferences.getBoolean(key, defaultValue)
    }

    override fun <T> putList(key: String, list: List<T>) {
        val jsonString = Gson().toJson(list)
        encryptedSharedPreferences.edit()
            .putString(key, jsonString)
            .apply()
    }

    override fun <T> getList(key: String): List<T> {
        val jsonString = encryptedSharedPreferences.getString(key, "[]")
        return Gson().fromJson<List<T>>(jsonString, List::class.java)
    }

    override fun remove(key: String) {
        encryptedSharedPreferences.edit()
            .remove(key)
            .apply()
    }

    override fun removeAllKeys() {
        val editor = encryptedSharedPreferences.edit()
        encryptedSharedPreferences.all.keys.forEach { editor.remove(it) }
        editor.apply()
    }
}
