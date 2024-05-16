package com.tdg.vroom.view.conferenceMeet.jitsi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.ext.requestNeededPermissions
import com.tdg.vroom.view.conferenceMeet.livekit.LiveKitMeetConferenceActivity
import org.jitsi.meet.sdk.BroadcastEvent
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate
import org.jitsi.meet.sdk.JitsiMeetActivityInterface
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetView
import timber.log.Timber
import java.net.URL


class JitsiMeetConferenceActivity : AppCompatActivity(),
    JitsiMeetActivityInterface {

    private var jitsiMeetView: JitsiMeetView? = null

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceivedMeetConference(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNeededPermissions {
            initMeetConference()
        }
    }

    private fun initMeetConference() {
        jitsiMeetView = JitsiMeetView(this)
        val options: JitsiMeetConferenceOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(URL(BuildConfig.JITSI_MEET_URL))
            .setRoom(intent?.getStringExtra(KEY_INPUT_ROOM_NAME).toString())
            .setAudioMuted(false)
            .setVideoMuted(false)
            .setAudioOnly(false)
            .setConfigOverride("requireDisplayName", true)
//            .setToken("MyJWT")
//            .setFeatureFlag("toolbox.enabled", false)
//            .setFeatureFlag("filmstrip.enabled", false)
            .build()
        jitsiMeetView?.join(options)
        setContentView(jitsiMeetView)
        registerForBroadcastMessages()
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }


    private fun onBroadcastReceivedMeetConference(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.type) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i(
                    "Conference Joined with url%s",
                    event.data["url"]
                )

                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i(
                    "Participant joined%s",
                    event.data["name"]
                )

                BroadcastEvent.Type.READY_TO_CLOSE -> {
                    endMeetConference()
                }

                else -> Timber.i("Received event: %s", event.type)
            }
        }
    }

    private fun endMeetConference() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        jitsiMeetView?.dispose()
        jitsiMeetView = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
        finish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(
            this, requestCode, resultCode, data
        )
    }

    override fun onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
        jitsiMeetView?.dispose()
        jitsiMeetView = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun requestPermissions(p0: Array<out String>?, p1: Int, p2: PermissionListener?) {

    }

    override fun onResume() {
        super.onResume()
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }

    companion object {
        const val KEY_INPUT_ROOM_NAME = "roomName"
    }

}