package me.apqx.libnet.tools

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import kotlin.experimental.and

class SerialToolsTest {

    @Before
    fun setUp() {
    }

    @Test
    fun byteToHexString() {
        assertEquals("01 02 02", SerialTools.byteToHexString(byteArrayOf(1, 2, 2)))
        assertEquals("FF 23 12", SerialTools.byteToHexString(byteArrayOf(0xff.toByte(), 0x23, 0x12)))
    }
}