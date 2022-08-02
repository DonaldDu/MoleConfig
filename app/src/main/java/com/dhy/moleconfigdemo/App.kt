package com.dhy.moleconfigdemo

import android.app.Application
import com.dhy.moleconfig.KvStore
import com.dhy.moleconfig.MoleConfig
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        val kv = MMKV.defaultMMKV()
        val store = KvStore(kv)
        MoleConfig.initDataHandler(store)
    }
}