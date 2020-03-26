package me.apqx.socket

import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.ArrayBlockingQueue

/**
 * 客户端，通过Socket连接Server进行通信
 *
 * @param bufferQueueSize 发送、接收暂存队列的容量，默认是20
 */
class Client(private val bufferQueueSize: Int = 20, private val log: ILog) {
    private lateinit var socketHandler: SocketHandler

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
        socketHandler = SocketHandler(Socket(InetAddress.getByName(ipStr), port), log)
    }

    /**
     * 发送指定的字节数组，阻塞方法，发送成功或抛出异常
     */
    fun send(byteArray: ByteArray) {

    }

    fun sendAndReceive(byteArray: ByteArray, overtimeSec: Int) {

    }


}