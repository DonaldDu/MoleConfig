package com.dhy.moleconfig

import android.content.SharedPreferences
import android.os.Parcelable
import com.tencent.mmkv.MMKV

class KvStore(private val kv: MMKV) : SharedPreferences by kv, SharedPreferences.Editor by kv, DataStore {
    override fun getDouble(key: String, defaultValue: Double): Double {
        return kv.decodeDouble(key, defaultValue)
    }

    override fun putDouble(key: String, value: Double) {
        kv.encode(key, value)
    }

    override fun putParcelable(key: String, value: Parcelable?) {
        kv.encode(key, value)
    }

    override fun <T : Parcelable> getParcelable(key: String, value: Class<T>): T? {
        return kv.decodeParcelable(key, value)
    }

    override fun getBytes(key: String, defaultValue: ByteArray?): ByteArray? {
        return kv.getBytes(key, defaultValue)
    }

    override fun putBytes(key: String?, bytes: ByteArray?) {
        kv.putBytes(key, bytes)
    }
}