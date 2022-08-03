package com.dhy.moleconfigdemo.data

import com.dhy.moleconfig.DefaultConfig
import com.dhy.moleconfig.IMoleConfig

interface UserConfig : IMoleConfig {
    var serializable: AccountS?
    var parcelable: AccountP?

    var string: String
    var int: Int
    var long: Long
    var float: Float
    var double: Double
    var boolean: Boolean

    @DefaultConfig("123")
    var stringDefaultConfig: String

    @DefaultConfig("123")
    var intDefaultConfig: Int

    @DefaultConfig("123")
    var longDefaultConfig: Long

    @DefaultConfig("123")
    var floatDefaultConfig: Float

    @DefaultConfig("123")
    var doubleDefaultConfig: Double

    @DefaultConfig("true")
    var booleanDefaultConfig: Boolean
}