package com.zaze.utils.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 将 时间戳 转为 一定格式的 日期字符串
 * @param pattern
 * @return
 */
fun Long.toDateString(
    pattern: String = DateUtil.DEFAULT_PATTERN,
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return Date(this).toDateString(pattern, timeZone)
}

/**
 * 将 Date 转为指定格式的字符串 输出。
 */
fun Date.toDateString(
    pattern: String = DateUtil.DEFAULT_PATTERN,
    timeZone: TimeZone = TimeZone.getDefault()
): String {
    return pattern.toDateFormat(timeZone).format(this)
}

/**
 * 将字符串转为 Date
 * @param pattern 日期格式
 * @return date
 */
fun String?.toDate(
    pattern: String,
    timeZone: TimeZone = TimeZone.getDefault()
): Date? {
    if (this.isNullOrEmpty()) {
        return null
    }
    try {
        return pattern.toDateFormat(timeZone).parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}

// -----------------
/**
 * @param timeZone 时区
 * @return 一天的开始时间
 */
fun Long.getDayStartMillis(timeZone: TimeZone = TimeZone.getDefault()): Long {
    val calendar = this.toCalendar(timeZone)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

/**
 * @param timeZone 时区
 * @return 一天的结束时间
 */
fun Long.getDayEndMillis(timeZone: TimeZone = TimeZone.getDefault()): Long {
    val calendar = this.toCalendar(timeZone)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 59)
    return calendar.timeInMillis
}

// -----------------
/**
 * 将 string 转为 SimpleDateFormat
 * @param timeZone 时区
 * @return SimpleDateFormat
 */
fun String.toDateFormat(
    timeZone: TimeZone = TimeZone.getDefault()
): SimpleDateFormat {
    return SimpleDateFormat(this, Locale.getDefault()).apply {
        this.timeZone = timeZone
    }
}


/**
 * 将 Date 转为 Calendar
 * @param timeZone 时区
 * @return Calendar
 */
fun Date.toCalendar(timeZone: TimeZone = TimeZone.getDefault()): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.timeZone = timeZone
    return calendar
}

/**
 * 将 时间戳 转为 Calendar
 * @param timeZone 时区
 * @return Calendar
 */
fun Long.toCalendar(timeZone: TimeZone = TimeZone.getDefault()): Calendar {
    return Date(this).toCalendar(timeZone)
}