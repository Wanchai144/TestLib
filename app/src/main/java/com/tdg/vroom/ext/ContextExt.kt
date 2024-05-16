package com.tdg.vroom.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.tdg.vroom.R
import com.tdg.vroom.view.conferenceMeet.jitsi.JitsiMeetConferenceActivity
import com.tdg.vroom.view.conferenceMeet.livekit.LiveKitMeetConferenceActivity


inline fun <reified AC : Activity> Context.launchActivity() {
    val intent = Intent(this, AC::class.java)
    this.startActivity(intent)
}

inline fun <reified AC : Activity> Context.launchActivity(keyExtra: Pair<String, String>) {
    val intent = Intent(this, AC::class.java)
    intent.putExtra(keyExtra.first, keyExtra.second)
    this.startActivity(intent)
}

fun Context.startIntentWebView(url: String) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    this.startActivity(i)
}

fun Context.startIntentShare(url: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_SEND
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, url)
    startActivity(
        Intent.createChooser(
            intent,
            "${this.resources.getString(R.string.app_name)} Share"
        )
    )
}

fun Context.startIntentPDF(pdfLink: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(Uri.parse("http://docs.google.com/viewer?url=$pdfLink"), "text/html")
    this.startActivity(intent)
}

fun Context.startIntentPlayStore(url: String) {
    try {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    } catch (e: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}

fun Context.startIntentMeetConferenceLiveKit(roomName: String) {
    this.launchActivity<LiveKitMeetConferenceActivity>(
        keyExtra =
        Pair(
            JitsiMeetConferenceActivity.KEY_INPUT_ROOM_NAME,
            roomName
        )
    )
}

fun Context.startIntentMeetConferenceJitsi(roomName: String) {
    this.launchActivity<JitsiMeetConferenceActivity>(
        keyExtra =
        Pair(
            JitsiMeetConferenceActivity.KEY_INPUT_ROOM_NAME,
            roomName
        )
    )
}