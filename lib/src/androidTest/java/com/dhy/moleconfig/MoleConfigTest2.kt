package com.dhy.moleconfig

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tencent.mmkv.MMKV
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoleConfigTest2 {
    private val kv: MMKV by lazy {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        MMKV.initialize(context)
        MMKV.defaultMMKV()
    }

    @Before
    fun setUp() {
        kv.clearAll()
        if (MoleConfig.dataHandlers.isEmpty()) {
            MoleConfig.initDataHandler(KvStore(kv))
        }
    }

    @Test
    fun testMe() {
        val user: UserConfig = getMoleConfigInstance()

        Assert.assertEquals("123", user.title)
        Assert.assertEquals(null, user.content)
        user.content = "1"
        Assert.assertEquals("1", user.content)

        Assert.assertEquals(0, user.code)
        user.code = 1
        Assert.assertEquals(1, user.code)

        Assert.assertEquals(null, user.accountS?.name)
        user.accountS = AccountS().apply { name = "123" }
        Assert.assertEquals("123", user.accountS?.name)

        Assert.assertEquals(null, user.accountP?.name)
        user.accountP = AccountP().apply { name = "123" }
        Assert.assertEquals("123", user.accountP?.name)
    }
}

