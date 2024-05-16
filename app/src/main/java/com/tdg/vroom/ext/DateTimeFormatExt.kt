package com.tdg.vroom.ext

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import com.tdg.vroom.R
import com.tdg.vroom.data.model.RecentModel
import com.tdg.vroom.ext.DateTimeFormatExt.Companion.FORMAT_DEFAULT_DATE
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale


class DateTimeFormatExt {
    companion object {
        const val FORMAT_DEFAULT_DATE = "dd/MM/yyyy"
        const val FORMAT_DEFAULT_MONTH = "MM/yyyy"
        const val FORMAT_SHORT_DATE = "dd/MM/yy"
        const val FORMAT_DEFAULT_TIME = "HH:mm:ss"
        const val FORMAT_SERVER_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        const val FORMAT_SERVER_DATE_SHORT = "yyyy-MM-dd'T'HH:mm:ss"
        const val FORMAT_SHORT_MONTH = "MM"
        const val FORMAT_NUMBER_MONTH = "M"
        const val FORMAT_MONTH = "MMMM"
        const val FORMAT_YEAR = "yyyy"
        const val FORMAT_SHORT_TIME = "HH:mm"
        const val FORMAT_SHORT_TIME_HOURS = "HH:mm"
        const val FORMAT_SHORT_DAY = "dd"
        const val FORMAT_NUMBER_DAY = "d"
        const val FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm"
        const val FORMAT_DATE_TIME_SECOND = "dd/MM/yyyy HH:mm:ss"
        const val FORMAT_FULL_DATE_TIME = "dd/MM/yy HH:mm"
        const val FORMAT_FULL_DATE_TIME_TISCO = "dd/MM/yyyy • HH:mm"
        const val FORMAT_HRMS_DATE = "dd MMMM yyyy"
        const val FORMAT_DATE_DAY_YEAR = "dd MMM"
        const val FORMAT_MONTH_YEAR = "yyyy"
        const val FORMAT_SHORT_DATE_2 = "yyyy-MM-dd"
        const val FORMAT_FULL_DATE = "yyyyMMdd_HHmmss"
        var timeStamp = getCurrentTimeStamp()
    }
}

@SuppressLint("SimpleDateFormat")
fun ArrayList<RecentModel>.filterRangeDateAfter7Date(): List<RecentModel> {
    val filterRangeDateAfter7Date = this.filter {
        val dtStart = it.date
        val format = SimpleDateFormat(FORMAT_DEFAULT_DATE)
        val dateNextWeek = format.parse(dtStart)

        val nextDays = 7
        val cal: Calendar = GregorianCalendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, nextDays)
        val sevenDaysAgo
        = cal.time

        dateNextWeek.before(sevenDaysAgo)
    }
    return filterRangeDateAfter7Date
}

fun isMoreThanCurrentDay(date: String): Boolean {
    return try {
        if (date.isNotEmpty()) {
            val formatter =
                SimpleDateFormat(DateTimeFormatExt.FORMAT_DEFAULT_DATE, Locale.US)
            val newDate: Date? = formatter.parse(date)
            val now = Date()
            newDate?.after(now) == true
        } else {
            false
        }
    } catch (ex: Exception) {
        false
    }
}


fun String.getFormatDate(inputFormat: String?, outputFormat: String?): String {
    val dateToFormat = this
    try {
        return SimpleDateFormat(inputFormat, Locale.US).parse(dateToFormat)?.let {
            SimpleDateFormat(outputFormat, Locale.US).format(
                it
            )
        } ?: ""
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return dateToFormat
}

@SuppressLint("SimpleDateFormat")
fun parseDateToddMMyyyy(time: Long? = 0L): String {
    val millisecond: Long = time ?: 0L
    return DateFormat.format(FORMAT_DEFAULT_DATE, Date(millisecond)).toString()
}

@SuppressLint("SimpleDateFormat")
fun Context.getYesterdayDateString(currentDate: String): String {
    val dateFormat = SimpleDateFormat(FORMAT_DEFAULT_DATE)
    val cal = Calendar.getInstance()
    cal.add(Calendar.DATE, -1)
    return if (dateFormat.format(cal.time) == currentDate)
        this.resources.getString(R.string.title_text_yesterday)
    else
        this.resources.getString(R.string.title_text_earlier)
}

fun getDateFromTimeStamp(outputFormat: String?): String {
    val currentTime = Calendar.getInstance().time
    return DateFormat.format(outputFormat, currentTime).toString()
}

fun Long.getDate(outputFormat: String?): String {
    val formatter = SimpleDateFormat(outputFormat, Locale.US)
    return formatter.format(this)
}

fun getCurrentTimeStamp(): String = (System.currentTimeMillis() / 1000).toString()
