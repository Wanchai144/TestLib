package com.tdg.vroom.data.local.preferences


interface SharePreferenceUtility {
    fun getString(key: String, defaultValue: String = ""): String
    fun putString(key: String, value: String)
    fun putLong(key: String, value: Long)
    fun putInt(key: String, value: Int)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun getLong(key: String, default: Long = 0): Long
    fun getInt(key: String, default: Int = 0): Int
    fun putBoolean(key: String, value: Boolean)
    fun <T> putList(key: String, list: List<T>)
    fun <T> getList(key: String): List<T>
    fun remove(key: String)
    fun removeAllKeys()
}
