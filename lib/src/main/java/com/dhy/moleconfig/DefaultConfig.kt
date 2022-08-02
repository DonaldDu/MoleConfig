package com.dhy.moleconfig

import java.lang.reflect.Method
import java.util.*

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class DefaultConfig(val value: String)

private val defaultConfigs: WeakHashMap<Method, DefaultConfig> = WeakHashMap()
val Method.defaultConfig: DefaultConfig?
    get() {
        var dc = defaultConfigs[this]
        if (dc != null) return dc

        val key = name.substring(3).replaceFirstChar { it.lowercase() }
        val kClass = Class.forName(declaringClass.name).kotlin
        val member = kClass.members.find { it.name == key }
        dc = member?.annotations?.find { it is DefaultConfig } as? DefaultConfig

        defaultConfigs[this] = dc
        return dc
    }