package com.dhy.moleconfig

import org.junit.AfterClass
import org.junit.Assert
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
            MoleConfig.initDataHandler(TempStore.dataStore)
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
    fun testMe() {
        val config: UserConfig = getMoleConfigInstance()

        Assert.assertEquals("123", config.defaultConfig)
        Assert.assertEquals(null, config.string)
        config.string = "1"
        Assert.assertEquals("1", config.string)

        Assert.assertEquals(0, config.int)
        config.int = 1
        Assert.assertEquals(1, config.int)

        Assert.assertEquals(0L, config.long)
        config.long = 1
        Assert.assertEquals(1L, config.long)

        Assert.assertEquals(0f, config.float)
        config.float = 1f
        Assert.assertEquals(1f, config.float)

        Assert.assertEquals(0.0, config.double)
        config.double = 1.0
        Assert.assertEquals(1.0, config.double)

        Assert.assertEquals(false, config.boolean)
        config.boolean = true
        Assert.assertEquals(true, config.boolean)

        Assert.assertEquals(null, config.accountS?.name)
        val accountS = AccountS().apply { name = "123" }
        config.accountS = accountS
        Assert.assertEquals(accountS.name, config.accountS?.name)

        Assert.assertEquals(null, config.accountP?.name)
        val accountP = AccountP().apply { name = "123" }
        config.accountP = accountP
        Assert.assertEquals(accountP.name, config.accountP?.name)
    }
}

private object TempStore {
    private val data: MutableMap<String, Any?> = mutableMapOf()
    private val handler = InvocationHandler { _, method, args ->
        val key = args.first().toString()
        if (method.isGet) {
            val v = data[key]
            if (v != null) v
            else {
                val p = args[1]
                if (p !is Class<*>) p else null
            }
        } else {
            data[key] = args[1]
            null
        }
    }
    val dataStore = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(DataStore::class.java), handler) as DataStore
}
