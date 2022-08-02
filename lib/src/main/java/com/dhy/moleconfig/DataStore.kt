package com.dhy.moleconfig

import android.content.SharedPreferences
import android.os.Parcelable

interface DataStore : SharedPreferences, SharedPreferences.Editor {
    fun getDouble(key: String, defaultValue: Double): Double
    fun putDouble(key: String, value: Double)

    fun <T : Parcelable> getParcelable(key: String, value: Class<T>): T?
    fun putParcelable(key: String, value: Parcelable?)

    fun getBytes(key: String, defaultValue: ByteArray?): ByteArray?
    fun putBytes(key: String?, bytes: ByteArray?)
}