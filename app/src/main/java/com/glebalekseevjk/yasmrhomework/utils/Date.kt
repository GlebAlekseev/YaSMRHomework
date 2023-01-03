package com.glebalekseevjk.yasmrhomework.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDateFromTimestamp(timestamp: Long, outputPatter: String): String {
    val sdf = SimpleDateFormat(outputPatter)
    val netDate = Date(timestamp)
    return sdf.format(netDate)
}

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}