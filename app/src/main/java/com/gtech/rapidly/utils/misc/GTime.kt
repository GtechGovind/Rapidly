package com.gtech.rapidly.utils.misc

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object GTime {

    fun toTime(epochTime: Long, format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(epochTime)
    }

    fun Timestamp.toTime(format: String): String {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        return dateFormat.format(this.toDate())
    }

    fun getDiffInMinute(timeToCalDiff: Long): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val diffMillis = currentTimeMillis - timeToCalDiff
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        return minutes.toInt()
    }

    fun Timestamp.diffInMinute(): Int {
        val currentTimeMillis = System.currentTimeMillis()
        val diffMillis = currentTimeMillis - this.seconds * 1000
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        return minutes.toInt()
    }

    fun diffInMinute(one: Long, two: Long): Int {
        val diffMillis = one - two
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis)
        return minutes.toInt()
    }

}