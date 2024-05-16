package com.tdg.vroom.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tdg.vroom.data.local.ConstantsRoom

@Entity(
    tableName = ConstantsRoom.TB_CALENDAR_MEETING
)
data class CalendarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    @SerializedName("eventId")
    val eventId: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("eventLocation")
    val eventLocation: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("dtStart")
    val dtStart: String?,
    @SerializedName("dtEnd")
    val dtEnd: String?,
    @SerializedName("duration")
    val duration: String?,
    @SerializedName("allDay")
    val allDay: Boolean?,
    @SerializedName("availability")
    val availability: Int?,
    @SerializedName("rRule")
    val rRule: String?,
    @SerializedName("displayColor")
    val displayColor: Int?,
    @SerializedName("visible")
    val visible: Boolean?,
    @SerializedName("itemsList")
    val itemsList: String? = "",
    @SerializedName("isCalendar")
    val isCalendar: Boolean?,
    @SerializedName("itemsListId")
    val itemsListId: String? = "",
    @SerializedName("itemsListDate")
    val itemsListDate: String? = "",
    @SerializedName("itemsListCalendar")
    val itemsListCalendar: String? = "",
    @SerializedName("itemsListContactTime")
    val itemsListContactTime: String? = ""
)