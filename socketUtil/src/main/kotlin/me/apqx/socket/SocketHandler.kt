package me.apqx.socket

import me.apqx.socket.tools.ByteTools
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

/**
 * 默认超时时间，秒
 */
const val OVERTIME_SEC = 5
/**
 * 发送、接收缓存大小
 */
const val BUFFER_SIZE = 1024
/**
 * 接收缓存队列大小
 */
const val QUEUE_SIZE_RECEIVE = 20

class SocketHandler(private val socket: Socket, private val iLog: ILog) {

    private var iBytesReceiveListener: IBytesReceiveListener? = null
    private var readThread: Thread? = null
    private val socketChannel: SocketChannel = socket.channel
    private val sendBuffer = ByteBuffer.allocate(BUFFER_SIZE)
    private val receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE)
    private val receiveBytesQueue: Queue<ByteArray> = LinkedList()

    /**
     * 发送字节数组，阻塞方法
     *
     * @param bytes 发送数据
     */
    fun send(bytes: ByteArray) {
        iLog.d("=> ${ByteTools.byteToHexString(bytes)}")
        sendBuffer.clear()
        sendBuffer.put(bytes)
        sendBuffer.flip()
        socketChannel.write(sendBuffer)
    }

    /**
     * 发送字节数组，并在指定时间内收取数据，超时无数据则返回空数组，阻塞方法
     *
     * @param bytes 发送数据
     * @param overTimeSec 超时时间，秒
     */
    fun sendAndReceive(bytes: ByteArray, overTimeSec: Int = OVERTIME_SEC): ByteArray {
        send(bytes)
        TODO()
    }

    /**
     * 监听收到的数据
     */
    fun setOnBytesReceiveListener(iBytesReceiveListener: IBytesReceiveListener) {
        this.iBytesReceiveListener = iBytesReceiveListener
    }

    /**
     * 开始从Socket中读取数据，开启一个独立的读取线程
     */
    fun startRead() {
        if (readThread == null) {
            readThread = Thread {
                while (!Thread.currentThread().isInterrupted) {
                    val bytes = readChannel(socketChannel, receiveBuffer)
                    if (bytes.isNotEmpty()) {
                        saveBytes2Queue(bytes)
                    }


                }
            }
        }
        readThread!!.start()
    }

    /**
     * 将收到的数据存入缓存队列
     */
    private fun saveBytes2Queue(bytes: ByteArray) {
        if (receiveBytesQueue.size == QUEUE_SIZE_RECEIVE) {
            iLog.d("")
        }
    }

    private fun readChannel(socketChannel: SocketChannel, receiveBuffer: ByteBuffer?): ByteArray {
        TODO()
    }

    /**
     * 停止从Socket中读取数据，中断读取线程
     */
    fun stopRead() {
        readThread?.interrupt()
    }

    /**
     * 关闭Socket连接
     */
    fun closeSocket() {
        socket.close()
    }
}