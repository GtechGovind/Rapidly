package com.gtech.rapidly.utils.misc

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

object GTime {

    fun toTime(epochTime: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return dateFormat.format(epochTime)
    }

    fun getDiffInMinute(timeToCalDiff: Long): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val diffMillis = currentTimeMillis - timeToCalDiff
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        return minutes.toInt()
    }

    fun diffInMinute(one: Long, two: Long): Int {
        val diffMillis = one - two
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        return minutes.toInt()
    }

}