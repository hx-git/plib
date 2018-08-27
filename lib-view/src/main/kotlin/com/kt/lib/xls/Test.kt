package com.kt.lib.xls


class Test{
    @Column(value = 1)
    private lateinit var name:String
    @Column(value = 5)
    private lateinit var address:String
    @Column(value = 4)
    private lateinit var city:String

    constructor()

    override fun toString(): String {
        return "Test(name='$name', address='$address', city='$city')"
    }

}