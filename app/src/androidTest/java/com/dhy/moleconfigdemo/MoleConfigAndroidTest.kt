package com.dhy.moleconfigdemo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dhy.moleconfig.KvStore
import com.dhy.moleconfig.MoleConfig
import com.dhy.moleconfig.getMoleConfigInstance
import com.dhy.moleconfigdemo.data.AccountP
import com.dhy.moleconfigdemo.data.AccountS
import com.dhy.moleconfigdemo.data.UserConfig
import com.tencent.mmkv.MMKV
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 特别说明：minifyEnabled true 可能造成，实际运行应用正常，测试却找不到类的问题。
 * */
@RunWith(AndroidJUnit4::class)
class MoleConfigAndroidTest {
    private val kv: MMKV by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        MMKV.initialize(context)
        MMKV.defaultMMKV()
    }

    @Before
    fun setUp() {
        kv.clearAll()
        if (MoleConfig.typeHandlers.isEmpty()) {
            MoleConfig.initTypeHandler(KvStore(kv))
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

        assertEquals(null, config.string)
        config.string = "1"
        assertEquals("1", config.string)

        assertEquals(0, config.int)
        config.int = 1
        assertEquals(1, config.int)

        assertEquals(0L, config.long)
        config.long = 1
        assertEquals(1L, config.long)

        assertEquals(0f, config.float)
        config.float = 1f
        assertEquals(1f, config.float)

        assertEquals(0.0, config.double, 0.0)
        config.double = 1.0
        assertEquals(1.0, config.double, 0.0)

        assertEquals(false, config.boolean)
        config.boolean = true
        assertEquals(true, config.boolean)

        assertEquals(null, config.serializable?.name)
        val accountS = AccountS().apply { name = "123" }
        config.serializable = accountS
        assertEquals(accountS.name, config.serializable?.name)

        assertEquals(null, config.parcelable?.name)
        val accountP = AccountP().apply { name = "123" }
        config.parcelable = accountP
        assertEquals(accountP.name, config.parcelable?.name)
    }
}

