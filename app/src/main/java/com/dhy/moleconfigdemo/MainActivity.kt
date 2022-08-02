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
        val config: UserConfig = getMoleConfigInstance()
        println(config.int)
        config.int = 1

        val accountP = AccountP().apply { name = "123" }
        config.accountP = accountP

        val accountS = AccountS().apply { name = "123" }
        config.accountS = accountS
    }
}