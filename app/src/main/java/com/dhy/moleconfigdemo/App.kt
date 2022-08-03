package com.dhy.moleconfigdemo

import androidx.multidex.MultiDexApplication
import com.dhy.moleconfig.KvStore
import com.dhy.moleconfig.MoleConfig
import com.tencent.mmkv.MMKV

class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        val kv = MMKV.defaultMMKV()
        val store = KvStore(kv)
        MoleConfig.initTypeHandler(store)
    }
}