package com.zaze.utils.date


import android.text.TextUtils
import com.zaze.utils.ZStringUtil
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Description :
 * date : 2016-04-21 - 12:11
 *
 * @author : zaze
 * @version : 1.0
 */
object DateUtil {
    const val SECOND = 1000L
    const val MINUTE = 60000L
    const val HOUR = 3600000L
    const val DAY = 86400000L
    const val WEEK = 604800000L
    const val HALF_YEAR = 15768000000L
    const val YEAR = 31536000000L

    // ----------------  about millis ------------------
    @JvmStatic
    fun getMinAndSec(timeMillis: Long): String {
        return ZStringUtil.format(
                "%02d' %02d'%s ", getMinute(timeMillis), getSecond(timeMillis), "'"
        )
    }

    @JvmStatic
    fun getHourAndMin(timeMillis: Long): String {
        return timeMillisToString(timeMillis, "HH:mm")
    }

    @JvmStatic
    fun getTimeMillisByHM(dateStr: String): Long {
        val date = stringToDate(dateStr, "HH:mm")
        val hour = getHour(date)
        val minute = getMinute(date)
        return 1000L * (hour * 3600 + minute * 60)
    }

    @JvmStatic
    fun getTimeMillisByHM(timeMillis: Long): Long {
        val date = timeMillisToDate(timeMillis)
        val hour = getHour(date)
        val minute = getMinute(date)
        return 1000L * (hour * 3600 + minute * 60)
    }

    // ----------------- about trans -----------------

    /**
     * @param dateStr String
     * @param pattern 日期格式
     * @return
     */
    @JvmStatic
    fun stringToDate(dateStr: String, pattern: String, timeZone: TimeZone = TimeZone.getDefault()): Date? {
        if (!TextUtils.isEmpty(dateStr)) {
            try {
                return getDateFormat(pattern, timeZone).parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

        }
        return null
    }

    /**
     * [date]    Date 对象
     * [pattern] 日期格式
     * [timeZone] timeZone
     * @return 转换特定格式的日期字符串
     */
    @JvmStatic
    fun dateToString(date: Date, pattern: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        return getDateFormat(pattern, timeZone).format(date)
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return
     */
    @JvmStatic
    fun timeMillisToString(timeMillis: Long, pattern: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        return dateToString(Date(timeMillis), pattern, timeZone)
    }

    @JvmStatic
    fun timeMillisToDate(timeMillis: Long): Date {
        return Date(timeMillis)
    }

    // ----------------  about int ------------------
    @JvmStatic
    fun getYear(date: Date): Int {
        return getInteger(date, Calendar.YEAR)
    }

    /**
     * @param date
     * @return day num
     */
    @JvmStatic
    fun getDay(date: Date): Int {
        return getInteger(date, Calendar.DATE)
    }

    /**
     * @param date
     * @return hour num
     */
    @JvmStatic
    fun getHour(date: Date?): Int {
        return getInteger(date, Calendar.HOUR_OF_DAY)
    }

    @JvmStatic
    fun getMinute(date: Date?): Int {
        return getInteger(date, Calendar.MINUTE)
    }

    @JvmStatic
    fun getMinute(timeMillis: Long): Int {
        return getInteger(timeMillis, Calendar.MINUTE)
    }

    @JvmStatic
    fun getSecond(date: Date): Int {
        return getInteger(date, Calendar.SECOND)
    }

    @JvmStatic
    fun getSecond(timeMillis: Long): Int {
        return getInteger(timeMillis, Calendar.SECOND)
    }


    // ----------------  about day ------------------

    /**
     * @param timeMillis
     * @return 一天的开始时间
     */
    @JvmStatic
    fun getDayStart(timeMillis: Long): Long {
        val calendar = getCalendar(Date(timeMillis))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    /**
     * @param timeMillis
     * @return 一天的结束时间
     */
    @JvmStatic
    fun getDayEnd(timeMillis: Long): Long {
        val calendar = getCalendar(Date(timeMillis))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 59)
        return calendar.timeInMillis
    }

    // ----------------  about week ------------------

    /**
     * @param timeMillis
     * @return (一周的开始)本周周一的开始时间
     */
    @JvmStatic
    fun getWeekStart(timeMillis: Long): Long {
        val week = getWeek(timeMillis)
        val day = week.getNumber()
        return getDayStart(timeMillis - (day - 1) * DAY)
    }

    /**
     * @param timeMillis
     * @return (一周的结束)本周周日的结束
     */
    @JvmStatic
    fun getWeekEnd(timeMillis: Long): Long {
        val week = getWeek(timeMillis)
        val day = week.getNumber()
        val endTime = getDayEnd(timeMillis)
        return endTime + (7 - day) * DAY
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return 星期
     */
    @JvmStatic
    fun getWeek(date: String, pattern: String): Week {
        return getWeek(stringToDate(date, pattern))
    }

    @JvmStatic
    fun getWeek(timeMillis: Long): Week {
        return getWeek(Date(timeMillis))
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date 日期
     * @return 星期
     */
    @JvmStatic
    fun getWeek(date: Date?): Week {
        val week: Week
        when (getInteger(date, Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> week = Week.SUNDAY
            Calendar.MONDAY -> week = Week.MONDAY
            Calendar.TUESDAY -> week = Week.TUESDAY
            Calendar.WEDNESDAY -> week = Week.WEDNESDAY
            Calendar.THURSDAY -> week = Week.THURSDAY
            Calendar.FRIDAY -> week = Week.FRIDAY
            Calendar.SATURDAY -> week = Week.SATURDAY
            else -> week = Week.MONDAY
        }
        return week
    }

    // ----------------  about month ------------------
    @JvmStatic
    fun calculateMonth(timeMillis: Long, offset: Int): Long {
        val calendar = getCalendar(Date(timeMillis))
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + offset)
        return calendar.timeInMillis
    }

    /**
     * 获取日期的月。失败返回null。
     *
     * @param date 日期
     * @return 星期
     */
    @JvmStatic
    fun getMonth(date: Date): Month {
        return when (getInteger(date, Calendar.MONTH)) {
            Calendar.JANUARY -> Month.JANUARY
            Calendar.FEBRUARY -> Month.FEBRUARY
            Calendar.MARCH -> Month.MARCH
            Calendar.APRIL -> Month.APRIL
            Calendar.MAY -> Month.MAY
            Calendar.JUNE -> Month.JUNE
            Calendar.JULY -> Month.JULY
            Calendar.AUGUST -> Month.AUGUST
            Calendar.SEPTEMBER -> Month.SEPTEMBER
            Calendar.OCTOBER -> Month.OCTOBER
            Calendar.NOVEMBER -> Month.NOVEMBER
            Calendar.DECEMBER -> Month.DECEMBER
            else -> Month.JANUARY
        }
    }
    // ---------------- private func ------------------

    /**
     * @param timeMillis 日期
     * @param dateType   年，月，日...
     * @return
     */
    private fun getInteger(timeMillis: Long, dateType: Int): Int {
        if (timeMillis > 0L) {
            val calendar = Calendar.getInstance()
            calendar.time = Date(timeMillis)
            return calendar.get(dateType)
        }
        return 0
    }

    /**
     * @param date     日期
     * @param dateType 年，月，日...
     * @return
     */
    private fun getInteger(date: Date?, dateType: Int): Int {
        if (date != null) {
            val calendar = getCalendar(date)
            return calendar.get(dateType)
        }
        return 0
    }

    /**
     * @param pattern 日期格式
     * @return SimpleDateFormat
     */
    private fun getDateFormat(pattern: String, timeZone: TimeZone = TimeZone.getDefault()): SimpleDateFormat {
        return SimpleDateFormat(pattern, Locale.getDefault()).apply {
            this.timeZone = timeZone
        }
    }

    /**
     * @param date 日期
     */
    private fun getCalendar(date: Date, timeZone: TimeZone = TimeZone.getDefault()): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.timeZone = timeZone
        return calendar
    }

}
