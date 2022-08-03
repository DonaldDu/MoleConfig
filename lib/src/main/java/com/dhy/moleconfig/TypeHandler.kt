package com.dhy.moleconfig

import android.os.Parcelable
import java.io.*
import java.lang.reflect.Method

interface TypeHandler<T> {
    fun get(method: Method, key: String): T?
    fun set(method: Method, key: String, value: Any?)
}

class IntHandler(private val store: DataStore) : TypeHandler<Int> {
    override fun get(method: Method, key: String): Int {
        return store.getInt(key, method.defaultConfig?.value?.toInt() ?: 0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putInt(key, value as Int)
        else store.remove(key)
    }
}

class LongHandler(private val store: DataStore) : TypeHandler<Long> {
    override fun get(method: Method, key: String): Long {
        return store.getLong(key, method.defaultConfig?.value?.toLong() ?: 0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putLong(key, value as Long)
        else store.remove(key)
    }
}

class FlotHandler(private val store: DataStore) : TypeHandler<Float> {
    override fun get(method: Method, key: String): Float {
        return store.getFloat(key, method.defaultConfig?.value?.toFloat() ?: 0f)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putFloat(key, value as Float)
        else store.remove(key)
    }
}

class BooleanHandler(private val store: DataStore) : TypeHandler<Boolean> {
    override fun get(method: Method, key: String): Boolean {
        return store.getBoolean(key, method.defaultConfig?.value?.toBoolean() ?: false)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putBoolean(key, value as Boolean)
        else store.remove(key)
    }
}

class DoubleHandler(private val store: DataStore) : TypeHandler<Double> {
    override fun get(method: Method, key: String): Double {
        return store.getDouble(key, method.defaultConfig?.value?.toDouble() ?: 0.0)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putDouble(key, value as Double)
        else store.remove(key)
    }
}

class StringHandler(private val store: DataStore) : TypeHandler<String> {
    override fun get(method: Method, key: String): String? {
        return store.getString(key, method.defaultConfig?.value)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putString(key, value as String)
        else store.remove(key)
    }
}

class ParcelableHandler(private val store: DataStore) : TypeHandler<Parcelable> {
    @Suppress("UNCHECKED_CAST")
    override fun get(method: Method, key: String): Parcelable? {
        return store.getParcelable(key, method.returnType as Class<Parcelable>)
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) store.putParcelable(key, value as Parcelable)
        else store.remove(key)
    }
}

class SerializableHandler(private val store: DataStore) : TypeHandler<Serializable?> {
    override fun get(method: Method, key: String): Serializable? {
        val data = store.getBytes(key, null)
        return if (data != null) ObjectInputStream(ByteArrayInputStream(data)).use { it.readObject() as Serializable } else null
    }

    override fun set(method: Method, key: String, value: Any?) {
        if (value != null) {
            val data = ByteArrayOutputStream()
            ObjectOutputStream(data).use { it.writeObject(value) }
            val bytes = data.toByteArray()
            store.putBytes(key, bytes)
        } else store.remove(key)
    }
}