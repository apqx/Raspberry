package me.apqx.logutil

/**
 * 平台日志输出接口，将写入文件的日志打印到控制台
 */
interface ILogPrint {

    fun d(tag: String, log: String)

    fun e(tag: String, log: String)
}