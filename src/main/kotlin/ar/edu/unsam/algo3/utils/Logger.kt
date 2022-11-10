package ar.edu.unsam.algo3.utils

import java.lang.Exception

object Logger {
    fun debug(tag: String, message: String) = println("D/$tag: $message")

    fun error(tag: String, message: String) = println("E/$tag: $message")

    fun error(tag: String, message: String, exception: Exception) {
        error(tag, message)
        exception.printStackTrace()
    }

    fun warning(tag: String, message: String) = println("W/$tag: $message")

    fun info(tag: String, message: String) = println("I/$tag: $message")
}