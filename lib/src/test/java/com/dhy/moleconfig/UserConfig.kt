package com.dhy.moleconfig


interface UserConfig : IMoleConfig {
    @DefaultConfig("123")
    var defaultConfig: String
    var serializable: AccountS?
    var parcelable: AccountP?

    var string: String?
    var int: Int?
    var long: Long?
    var float: Float?
    var double: Double?
    var boolean: Boolean?
}