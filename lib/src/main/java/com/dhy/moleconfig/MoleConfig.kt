package com.dhy.moleconfig

import android.os.Parcelable
import java.io.Serializable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

object MoleConfig {
    val typeHandlers: MutableMap<Class<*>, TypeHandler<*>> = mutableMapOf()
    var typeHandlerFinder: TypeHandlerFinder = object : TypeHandlerFinder {}
    var configKeyGenerator: ConfigKeyGenerator = object : ConfigKeyGenerator {}
    var configInvocationHandler: ConfigInvocationHandler = object : ConfigInvocationHandler {}

    fun initTypeHandler(kv: DataStore) {
        StringHandler(kv).install(String::class)
        IntHandler(kv).install(Integer::class)
        LongHandler(kv).install(java.lang.Long::class)
        FlotHandler(kv).install(java.lang.Float::class)
        BooleanHandler(kv).install(java.lang.Boolean::class)
        DoubleHandler(kv).install(java.lang.Double::class)
        ParcelableHandler(kv).install(Parcelable::class)
        SerializableHandler(kv).install(Serializable::class)
    }
}

fun TypeHandler<*>.install(type: KClass<*>) {
    MoleConfig.typeHandlers[type.java] = this

    val javaPrimitiveType = type.javaPrimitiveType
    if (javaPrimitiveType != null) MoleConfig.typeHandlers[javaPrimitiveType] = this
}

interface ConfigKeyGenerator {
    fun gen(method: Method): String {
        val key = method.name.substring(3)//getXXX or setXXX
        return "${method.declaringClass.name}/$key"
    }
}

interface TypeHandlerFinder {
    fun find(clazz: Class<*>): TypeHandler<*>? {
        return MoleConfig.typeHandlers[clazz] ?: if (clazz.isAssignableTo(Parcelable::class.java)) {
            MoleConfig.typeHandlers[Parcelable::class.java]
        } else if (clazz.isAssignableTo(Serializable::class.java)) {
            MoleConfig.typeHandlers[Serializable::class.java]
        } else null
    }
}

interface ConfigInvocationHandler {
    fun invoke(method: Method, args: Array<*>?): Any? {
        val typeHandler = MoleConfig.typeHandlerFinder.find(method.configType)
        return if (typeHandler != null) {
            val key = MoleConfig.configKeyGenerator.gen(method)
            if (method.isGet) {
                typeHandler.get(method, key)
            } else {
                typeHandler.set(method, key, args!!.first())
            }
        } else {
            throw IllegalStateException("no fit typeHandler found for :$method")
        }
    }
}

inline fun <reified T : KeepMoleConfig> getMoleConfigInstance(): T {
    return getMoleConfigInstance(T::class.java)
}

@Suppress("UNCHECKED_CAST")
fun <T : KeepMoleConfig> getMoleConfigInstance(configClass: Class<T>): T {
    val handler = InvocationHandler { _, method, args -> MoleConfig.configInvocationHandler.invoke(method, args) }
    return Proxy.newProxyInstance(configClass.classLoader, arrayOf(configClass), handler) as T
}

val Method.isGet: Boolean get() = name.startsWith("get")
private val Method.configType: Class<*>
    get() {
        return if (isGet) returnType
        else parameterTypes.first()
    }

fun Class<*>.isAssignableTo(parent: Class<*>): Boolean {
    return parent.isAssignableFrom(this)
}