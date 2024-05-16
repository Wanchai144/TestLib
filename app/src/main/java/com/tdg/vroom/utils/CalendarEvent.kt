package com.tdg.vroom.utils

import android.content.Context
import android.provider.CalendarContract
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.fragment.app.FragmentActivity
import com.tdg.vroom.data.local.room.entity.CalendarEntity
import com.tdg.vroom.ext.DateTimeFormatExt
import com.tdg.vroom.ext.getDate
import com.tdg.vroom.ext.getDateFromTimeStamp
import com.tdg.vroom.ext.getFormatDate
import com.tdg.vroom.ext.parseDateToddMMyyyy
import com.tdg.vroom.view.home.ui.menuCalendar.CalendarViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class CalendarEvent @Inject constructor(@ApplicationContext val context: Context) {

    private val EVENT_PROJECTION = arrayOf(
        CalendarContract.Events._ID,
        CalendarContract.Events.TITLE,
        CalendarContract.Events.DESCRIPTION,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTSTART,
        CalendarContract.Events.DTEND,
        CalendarContract.Events.DURATION,
        CalendarContract.Events.ALL_DAY,
        CalendarContract.Events.AVAILABILITY,
        CalendarContract.Events.RRULE,
        CalendarContract.Events.DISPLAY_COLOR,
        CalendarContract.Events.VISIBLE,
        CalendarContract.Events.ACCOUNT_NAME
    )
    private val PROJECTION_ID_INDEX = 0
    private val PROJECTION_TITLE_INDEX = 1
    private val PROJECTION_EVENT_LOCATION_INDEX = 2
    private val PROJECTION_STATUS_INDEX = 3
    private val PROJECTION_DTSTART_INDEX = 4
    private val PROJECTION_DTEND_INDEX = 5
    private val PROJECTION_DURATION_INDEX = 6
    private val PROJECTION_ALL_DAY_INDEX = 7
    private val PROJECTION_AVAILABILITY_INDEX = 8
    private val PROJECTION_RRULE_INDEX = 9
    private val PROJECTION_DISPLAY_COLOR_INDEX = 10
    private val PROJECTION_VISIBLE_INDEX = 11

    fun getCalendarEvent(context: FragmentActivity, calendarViewModel: CalendarViewModel) {
        val uri = CalendarContract.Events.CONTENT_URI
        val selection = "(${CalendarContract.Events.CALENDAR_ID} = ?)"
        val cur = context.contentResolver.query(
            uri,
            EVENT_PROJECTION,
            null,
            null,
            null,
        )

        if (cur != null) {
            while (cur.moveToNext()) {
                context.runOnUiThread {
                    val eventId = cur.getLong(PROJECTION_ID_INDEX)
                    val title = cur.getStringOrNull(PROJECTION_TITLE_INDEX) ?: ""
                    val eventLocation = cur.getStringOrNull(PROJECTION_EVENT_LOCATION_INDEX)
                    val status = cur.getString(PROJECTION_STATUS_INDEX)
                    val dtStart = parseDateToddMMyyyy(cur.getLongOrNull(PROJECTION_DTSTART_INDEX))
                    val dtEnd = parseDateToddMMyyyy(cur.getLongOrNull(PROJECTION_DTEND_INDEX))
                    val dtTime = cur.getLongOrNull(PROJECTION_DTSTART_INDEX)
                        ?.getDate(outputFormat = DateTimeFormatExt.FORMAT_SHORT_TIME)
                    val dtTimeEnd = cur.getLongOrNull(PROJECTION_DTEND_INDEX)
                        ?.getDate(outputFormat = DateTimeFormatExt.FORMAT_SHORT_TIME)
                    val duration = cur.getStringOrNull(PROJECTION_DURATION_INDEX)
                    val allDay = cur.getIntOrNull(PROJECTION_ALL_DAY_INDEX) == 1
                    val availability = cur.getIntOrNull(PROJECTION_AVAILABILITY_INDEX)
                    val rRule = cur.getStringOrNull(PROJECTION_RRULE_INDEX)
                    val displayColor = cur.getIntOrNull(PROJECTION_DISPLAY_COLOR_INDEX)
                    val visible = cur.getIntOrNull(PROJECTION_VISIBLE_INDEX) == 1

                    if (isCheckEventInCurrentMonth(dtStart)) {
                        if (dtEnd.getFormatDate(
                                DateTimeFormatExt.FORMAT_DEFAULT_DATE,
                                outputFormat = DateTimeFormatExt.FORMAT_SHORT_DAY
                            )
                                .toInt() >= getDateFromTimeStamp(outputFormat = DateTimeFormatExt.FORMAT_SHORT_DAY).toInt()
                        ) {
                            calendarViewModel.getListAllCalendarMeeting(dateMeeting = dtStart) {
                                if (it.isEmpty()) {
                                    val eventTime =
                                        if (dtTime != dtTimeEnd) "$dtTime - $dtTimeEnd" else ""

                                    MyLog.i(
                                        "eventId : $eventId\ntitle : $title\n date : $dtStart to $dtEnd" +
                                                "\nlocation : $eventLocation\n status : $dtTime to $dtTimeEnd = $eventTime \n duration : $duration" +
                                                "\nallDay : $allDay\n availability : $availability\n rRule : $rRule"
                                    )

                                    if (eventTime.isNotEmpty()) {
                                        calendarViewModel.saveCalendarFromEvent(
                                            param = CalendarEntity(
                                                id = eventId.toInt(),
                                                eventId.toString(),
                                                title,
                                                eventLocation,
                                                eventTime,
                                                dtStart,
                                                dtEnd,
                                                duration,
                                                allDay,
                                                availability,
                                                rRule,
                                                displayColor,
                                                visible,
                                                "",
                                                true
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        cur?.close()
        // calendarViewModel.requestCalendar()
    }

    private fun isCheckEventInCurrentMonth(dtStart: String): Boolean {
        return dtStart.contains(getDateFromTimeStamp(outputFormat = DateTimeFormatExt.FORMAT_DEFAULT_MONTH))
    }
}