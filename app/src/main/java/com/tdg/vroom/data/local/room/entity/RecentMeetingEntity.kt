package com.tdg.vroom.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.tdg.vroom.data.local.ConstantsRoom
import java.io.Serializable

@Entity(
    tableName = ConstantsRoom.TB_RECENT_MEETING
)
data class RecentMeetingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,
    @SerializedName("eventName")
    val eventName: String?,
    @SerializedName("eventDate")
    val eventDate: String?,
    @SerializedName("eventDateTime")
    val eventDateTime:String?,
    @SerializedName("itemsList")
    val itemsList: String? = ""
) : Serializable
