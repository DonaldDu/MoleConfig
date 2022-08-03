package com.dhy.moleconfig

import android.os.Parcelable
import java.io.*
import java.lang.reflect.Method

interface TypeHandler<T> {
    fun get(method: Method, key: String): T?
    fun set(method: Method, key: String, value: Any?)
}

class IntHandler(private val kv: DataStore) : TypeHandler<Int> {
    override fun get(method: Method, key: String): Int {
        return kv.getInt(key, method.defaultConfig?.value?.toInt() ?: 0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putInt(key, value as Int)
        else kv.remove(key)
    }
}

class LongHandler(private val kv: DataStore) : TypeHandler<Long> {
    override fun get(method: Method, key: String): Long {
        return kv.getLong(key, method.defaultConfig?.value?.toLong() ?: 0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putLong(key, value as Long)
        else kv.remove(key)
    }
}

class FlotHandler(private val kv: DataStore) : TypeHandler<Float> {
    override fun get(method: Method, key: String): Float {
        return kv.getFloat(key, method.defaultConfig?.value?.toFloat() ?: 0f)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putFloat(key, value as Float)
        else kv.remove(key)
    }
}

class BooleanHandler(private val kv: DataStore) : TypeHandler<Boolean> {
    override fun get(method: Method, key: String): Boolean {
        return kv.getBoolean(key, method.defaultConfig?.value?.toBoolean() ?: false)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putBoolean(key, value as Boolean)
        else kv.remove(key)
    }
}

class DoubleHandler(private val kv: DataStore) : TypeHandler<Double> {
    override fun get(method: Method, key: String): Double {
        return kv.getDouble(key, method.defaultConfig?.value?.toDouble() ?: 0.0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putDouble(key, value as Double)
        else kv.remove(key)
    }
}

class StringHandler(private val kv: DataStore) : TypeHandler<String> {
    override fun get(method: Method, key: String): String? {
        return kv.getString(key, method.defaultConfig?.value)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putString(key, value as String)
        else kv.remove(key)
    }
}

class ParcelableHandler(private val kv: DataStore) : TypeHandler<Parcelable> {
    @Suppress("UNCHECKED_CAST")
    override fun get(method: Method, key: String): Parcelable? {
        return kv.getParcelable(key, method.returnType as Class<Parcelable>)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) kv.putParcelable(key, value as Parcelable)
        else kv.remove(key)
    }
}

class SerializableHandler(private val kv: DataStore) : TypeHandler<Serializable?> {
    override fun get(method: Method, key: String): Serializable? {
        val data = kv.getBytes(key, null)
        return if (data != null) ObjectInputStream(ByteArrayInputStream(data)).use { it.readObject() as Serializable } else null
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) {
            val data = ByteArrayOutputStream()
            ObjectOutputStream(data).use { it.writeObject(value) }
            val bytes = data.toByteArray()
            kv.putBytes(key, bytes)
        } else kv.remove(key)
    }
}