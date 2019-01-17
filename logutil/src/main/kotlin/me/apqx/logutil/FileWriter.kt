package me.apqx.logutil

import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channel
import java.nio.channels.FileChannel

/**
 * 向指定的文件中写入信息的工具
 */
const val byteBufferCapacity = 1024

class FileWriter(private val dirPath: String, private val fileName: String) {
    private var fileChannel: FileChannel = FileOutputStream(File(dirPath, fileName), true).channel
    private val byteBuffer = ByteBuffer.allocate(1024)

    /**
     * 向文件中写入字符串，使用UTF-8编码
     */
    fun write(str: String) {
        // 默认使用UTF-8编码
        val byteArray = str.toByteArray()
        val count = byteArray.size / byteBufferCapacity
        for (i in 0..count) {
            if (i == count) {
                byteBuffer.put(byteArray.copyOfRange(i * byteBufferCapacity, byteArray.size % byteBufferCapacity))
            } else {
                byteBuffer.put(byteArray.copyOfRange(i * byteBufferCapacity, byteBufferCapacity))
            }
            fileChannel.write(byteBuffer)
        }

    }

    /**
     * 关闭通道，释放资源
     */
    fun close() {
        fileChannel.close()
    }
}