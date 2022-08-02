package com.dhy.moleconfig

import androidx.annotation.Keep

@Keep
interface UserConfig {
    @DefaultConfig("123")
    var title: String
    var content: String?
    var code: Int?
    var accountS: AccountS?
    var accountP: AccountP?
}