package com.glebalekseevjk.yasmrhomework.utils

import java.text.SimpleDateFormat
import java.util.*

fun getFormattedDateFromTimestamp(timestamp: Long, outputPatter: String): String {
    val sdf = SimpleDateFormat(outputPatter)
    val netDate = Date(timestamp)
    return sdf.format(netDate)
}