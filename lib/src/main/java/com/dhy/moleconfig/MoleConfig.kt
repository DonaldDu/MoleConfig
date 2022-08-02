package com.dhy.moleconfig

import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

object MoleConfig {
    val dataHandlers: MutableMap<Class<*>, DataHandler<*>> = mutableMapOf()
    var dataHandlerFinder: DataHandlerFinder = object : DataHandlerFinder {}
    var configKeyGenerator: ConfigKeyGenerator = object : ConfigKeyGenerator {}
    var configInvocationHandler: ConfigInvocationHandler = object : ConfigInvocationHandler {}

    fun initDataHandler(kv: DataStore) {
        StringDataHandler(kv).install(String::class)
        IntDataHandler(kv).install(Integer::class)
        LongDataHandler(kv).install(java.lang.Long::class)
        FlotDataHandler(kv).install(java.lang.Float::class)
        BooleanDataHandler(kv).install(java.lang.Boolean::class)
        DoubleDataHandler(kv).install(java.lang.Double::class)
        ParcelableDataHandler(kv).install(Parcelable::class)
        SerializableDataHandler(kv).install(Serializable::class)
    }
}

fun DataHandler<*>.install(type: KClass<*>) {
    MoleConfig.dataHandlers[type.java] = this

    val javaPrimitiveType = type.javaPrimitiveType
    if (javaPrimitiveType != null) MoleConfig.dataHandlers[javaPrimitiveType] = this
}

interface ConfigKeyGenerator {
    fun gen(method: Method): String {
        val key = method.name.substring(3)//getXXX or setXXX
        return "${method.declaringClass.name}/$key"
    }
}

interface DataHandlerFinder {
    fun find(clazz: Class<*>): DataHandler<*>? {
        return MoleConfig.dataHandlers[clazz] ?: if (clazz.isAssignableTo(Parcelable::class.java)) {
            MoleConfig.dataHandlers[Parcelable::class.java]
        } else if (clazz.isAssignableTo(Serializable::class.java)) {
            MoleConfig.dataHandlers[Serializable::class.java]
        } else null
    }
}

interface ConfigInvocationHandler {
    fun invoke(method: Method, args: Array<*>?): Any? {
        val dataHandler = MoleConfig.dataHandlerFinder.find(method.configType)
        return if (dataHandler != null) {
            val key = MoleConfig.configKeyGenerator.gen(method)
            if (method.isGet) {
                dataHandler.get(method, key)
            } else {
                dataHandler.set(method, key, args!!.first())
            }
        } else {
            throw IllegalStateException("no fit DataHandler found for :$method")
        }
    }
}

inline fun <reified T : IMoleConfig> getMoleConfigInstance(): T {
    return getMoleConfigInstance(T::class.java)
}

@Suppress("UNCHECKED_CAST")
fun <T : IMoleConfig> getMoleConfigInstance(configClass: Class<T>): T {
    val handler = InvocationHandler { _, method, args -> MoleConfig.configInvocationHandler.invoke(method, args) }
    return Proxy.newProxyInstance(configClass.classLoader, arrayOf(configClass), handler) as T
}

internal val Method.isGet: Boolean get() = name.startsWith("get")
private val Method.configType: Class<*>
    get() {
        return if (isGet) returnType
        else parameterTypes.first()
    }

fun Class<*>.isAssignableTo(parent: Class<*>): Boolean {
    return parent.isAssignableFrom(this)
}