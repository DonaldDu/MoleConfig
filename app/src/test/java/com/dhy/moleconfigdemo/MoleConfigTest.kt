package com.dhy.moleconfigdemo

import com.dhy.moleconfig.*
import com.dhy.moleconfigdemo.data.AccountP
import com.dhy.moleconfigdemo.data.AccountS
import com.dhy.moleconfigdemo.data.UserConfig
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.*

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class MoleConfigTest {
    companion object {
        @BeforeClass
        @JvmStatic
        fun setUp() {
            MoleConfig.initTypeHandler(TempStore.dataStore)
        }

        @AfterClass
        @JvmStatic
        fun end() {
            val defaultConfigsF = Class.forName(DefaultConfig::class.java.name + "Kt").getDeclaredField("defaultConfigs")
            defaultConfigsF.isAccessible = true
            val map = defaultConfigsF.get(null) as WeakHashMap<*, *>
            Assert.assertTrue(map.isNotEmpty())
        }
    }

    @Test
    fun usage() {
        val config: UserConfig = getMoleConfigInstance()

        assertEquals("123", config.stringDefaultConfig)
        assertEquals(123, config.intDefaultConfig)
        assertEquals(123L, config.longDefaultConfig)
        assertEquals(123f, config.floatDefaultConfig)
        assertEquals(123.0, config.doubleDefaultConfig, 0.0)
        assertEquals(true, config.booleanDefaultConfig)

        assertEquals(null, config.string)//default value
        config.string = "1"
        assertEquals("1", config.string)

        assertEquals(0, config.int)//default value
        config.int = 1
        assertEquals(1, config.int)

        assertEquals(0L, config.long)//default value
        config.long = 1
        assertEquals(1L, config.long)

        assertEquals(0f, config.float)//default value
        config.float = 1f
        assertEquals(1f, config.float)

        assertEquals(0.0, config.double, 0.0)//default value
        config.double = 1.0
        assertEquals(1.0, config.double, 0.0)

        assertEquals(false, config.boolean)//default value
        config.boolean = true
        assertEquals(true, config.boolean)

        assertEquals(null, config.serializable?.name)//default value
        val accountS = AccountS().apply { name = "123" }
        config.serializable = accountS
        assertEquals(accountS.name, config.serializable?.name)

        assertEquals(null, config.parcelable?.name)//default value
        val accountP = AccountP().apply { name = "123" }
        config.parcelable = accountP
        assertEquals(accountP.name, config.parcelable?.name)
    }
}

private object TempStore {
    private val data: MutableMap<String, Any?> = mutableMapOf()
    private val handler = InvocationHandler { _, method, args ->
        val key = args.first() as String
        if (method.isGet) {
            val v = data[key]
            if (v != null) v
            else {
                val defaultValue = args[1]
                if (defaultValue !is Class<*>) defaultValue else null
            }
        } else {
            data[key] = args[1]
            null
        }
    }
    val dataStore = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(DataStore::class.java), handler) as DataStore
}
