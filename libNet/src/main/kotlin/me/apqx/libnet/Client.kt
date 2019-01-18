package me.apqx.libnet

import java.net.Inet4Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.BlockingQueue

/**
 * 客户端，通过Socket连接Server进行通信
 *
 * @param bufferQueueSize 发送、接收暂存队列的容量，默认是20
 */
class Client(private val bufferQueueSize: Int = 20, private val log: INetLog) {

    private lateinit var socketChannel: SocketChannel
    private val byteBuffer = ByteBuffer.allocate(1024)
    /**
     * 发送信息的阻塞队列，所有要发送的信息都会立刻存在此队列中，由单独的工作线程执行发送操作
     */
    private val sendQueue = ArrayBlockingQueue<ByteArray>(bufferQueueSize)

    /**
     * 连接指定的ip和端口，阻塞方法，成功或抛出异常
     *
     * @param ipStr 目标ip
     * @param port 目标端口
     */
    fun connect(ipStr: String, port: Int) {
        socketChannel = SocketChannel.open(InetSocketAddress(ipStr, port))
    }

    /**
     * 发送指定的字节数组，将要发送的数据存入待发队列
     */
    fun send(byteArray: ByteArray) {
        if (sendQueue.size == bufferQueueSize - 5) {
            log.e("")
        }
    }


}