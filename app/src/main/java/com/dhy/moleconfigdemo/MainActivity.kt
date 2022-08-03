package com.dhy.moleconfigdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhy.moleconfig.getMoleConfigInstance
import com.dhy.moleconfigdemo.data.AccountP
import com.dhy.moleconfigdemo.data.AccountS
import com.dhy.moleconfigdemo.data.UserConfig

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        usage()
    }

    fun usage() {
        val config: UserConfig = getMoleConfigInstance()

        val newName = System.currentTimeMillis().toString()
        config.parcelable = AccountP().apply { name = newName }
        assert(config.parcelable?.name == newName)

        config.serializable = AccountS().apply { name = newName }
        assert(config.serializable?.name == newName)

        val now = System.currentTimeMillis()
        config.long = now
        assert(config.long == now)
    }
}