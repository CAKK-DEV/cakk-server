package com.cakk.common.utils

fun generateRandomStringOnlyNumber(length: Int): String {
    val sb = StringBuilder()

    for (i in 0 until length) {
        sb.append((Math.random() * 10).toInt())
    }

    return sb.toString()
}
