package com.tdg.vroom.view.conferenceMeet.livekit.dialog

import android.app.Activity
import android.app.AlertDialog
import android.widget.ArrayAdapter
import com.tdg.vroom.R
import com.tdg.vroom.view.conferenceMeet.livekit.LiveKitMeetConferenceViewModel

fun Activity.showSelectAudioDeviceDialog(callViewModel: LiveKitMeetConferenceViewModel) {
    val builder = with(AlertDialog.Builder(this)) {
        setTitle("Select Audio Device")

        val audioHandler = callViewModel.audioHandler
        val audioDevices = audioHandler.availableAudioDevices
        val arrayAdapter = ArrayAdapter<String>(this@showSelectAudioDeviceDialog, R.layout.select_dialog_item)
        arrayAdapter.addAll(audioDevices.map { it.name })
        setAdapter(arrayAdapter) { dialog, index ->
            audioHandler.selectDevice(audioDevices[index])
            dialog.dismiss()
        }
    }
    builder.show()
}
