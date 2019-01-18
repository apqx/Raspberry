package me.apqx.logutil

import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channel
import java.nio.channels.FileChannel

/**
 * 向指定的文件中写入信息的工具
 */
const val byteBufferCapacity = 1024

class FileWriter(dirPath: String, fileName: String) {
    var logFile: File = File(dirPath, fileName)
    private var fileChannel: FileChannel = FileOutputStream(logFile, true).channel
    private val byteBuffer = ByteBuffer.allocate(1024)

    fun redirect(logFile: File) {
        this.logFile = logFile
        close()
        fileChannel = FileOutputStream(logFile, true).channel
    }

    /**
     * 向文件中写入字符串，使用UTF-8编码
     */
    fun write(str: String) {
        // 默认使用UTF-8编码
        val byteArray = str.toByteArray()
        val count = byteArray.size / byteBufferCapacity
        byteBuffer.clear()
        for (i in 0..count) {
            if (i == count) {
                byteBuffer.put(byteArray.copyOfRange(i * byteBufferCapacity, byteArray.size % byteBufferCapacity))
            } else {
                byteBuffer.put(byteArray.copyOfRange(i * byteBufferCapacity, byteBufferCapacity))
            }
            // TODO:必须执行flip，否则输出的数据是之前第一次写入的数据
            byteBuffer.flip()
            if (fileChannel.isOpen) {
                fileChannel.write(byteBuffer)
            }
            byteBuffer.clear()
        }

    }

    /**
     * 关闭通道，释放资源
     */
    fun close() {
        fileChannel.close()
    }

}