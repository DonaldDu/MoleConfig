package com.dhy.moleconfigdemo.data

import com.dhy.moleconfig.DefaultConfig
import com.dhy.moleconfig.KeepMoleConfig

interface UserConfig : KeepMoleConfig {
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

    /**
     * 枚举默认用Serializable处理，
     * 类名及枚举名都会序列化为字节码后保存
     * */
    var color: ColorEnum?
}

enum class ColorEnum {
    WHITE, BLACK, RED
}