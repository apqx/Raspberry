package me.apqx.socket.tools

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class ByteToolsTest {

    @Before
    fun setUp() {
    }

    @Test
    fun byteToHexString() {
        assertEquals("01 02 02", ByteTools.byteToHexString(byteArrayOf(1, 2, 2)))
        assertEquals("FF 23 12", ByteTools.byteToHexString(byteArrayOf(0xff.toByte(), 0x23, 0x12)))
    }
}