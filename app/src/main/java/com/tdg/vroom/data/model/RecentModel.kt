package com.tdg.vroom.data.model

data class RecentModel(
    val title: String,
    val date: String,
    var dataMeet: ArrayList<SubRecentModel>
) {
    data class SubRecentModel(
        val eventId: String,
        val txtImage: String,
        val roomName: String,
        val description: String,
        val time: String,
        val backgroundColor: Int,
        val stateViewAddMeeting: Boolean = false
    )
}


