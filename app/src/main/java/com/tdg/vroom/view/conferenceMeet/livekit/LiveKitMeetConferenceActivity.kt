package com.tdg.vroom.view.conferenceMeet.livekit

import android.app.Activity
import android.media.projection.MediaProjectionManager
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseActivity
import com.tdg.vroom.databinding.ActivityLivekitMeetConferenceBinding
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.requestNeededPermissions
import com.tdg.vroom.ext.visible
import com.tdg.vroom.utils.dialog.DialogMeetOptionMenu
import com.tdg.vroom.view.adapter.OPTION_MEET_CLICK
import com.tdg.vroom.view.conferenceMeet.livekit.dialog.showDebugMenuDialog
import com.tdg.vroom.view.conferenceMeet.livekit.dialog.showSelectAudioDeviceDialog
import com.xwray.groupie.GroupieAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.tdg.vroom.view.conferenceMeet.livekit.dialog.showAudioProcessorSwitchDialog
import com.tdg.vroom.view.home.HomeVIewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LiveKitMeetConferenceActivity : BaseActivity<ActivityLivekitMeetConferenceBinding>() {

    private val viewModel: LiveKitMeetConferenceViewModel by viewModels()

    private val homeVIewModel: HomeVIewModel by viewModels()

    private val screenCaptureIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode != Activity.RESULT_OK || data == null) {
                return@registerForActivityResult
            }
            viewModel.startScreenCapture(data)
        }


    override fun initView() {
        setFullScreenStatusBarColor(R.color.colorBlack)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setLoadingViewConference(viewModel.loadingState)
        requestNeededPermissions {
            viewModel.requestConferenceLiveKit(
                url = BuildConfig.LIVEKIT_MEET_URL,
                roomName = intent?.getStringExtra(KEY_INPUT_ROOM_NAME).toString()
            )
            setUpMeetConference()
        }
    }

    private fun setUpMeetConference() {
        binding.tvRoomName.text = intent.getStringExtra(KEY_INPUT_ROOM_NAME)
        // Audience row setup
        val audienceAdapter = GroupieAdapter()
        binding.audienceRow.apply {
            layoutManager = LinearLayoutManager(
                this@LiveKitMeetConferenceActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = audienceAdapter
        }

        lifecycleScope.launchWhenCreated {
            viewModel.participants
                .collect { participants ->
                    val items = participants.map { participant ->
                        ParticipantItem(
                            viewModel.room,
                            participant
                        )
                    }
                    audienceAdapter.update(items)
                }
        }

        // speaker view setup
        val speakerAdapter = GroupieAdapter()
        binding.speakerView.apply {
            layoutManager = LinearLayoutManager(
                this@LiveKitMeetConferenceActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = speakerAdapter
        }
        lifecycleScope.launchWhenCreated {
            viewModel.primarySpeaker.collectLatest { speaker ->
                val items = listOfNotNull(speaker)
                    .map { participant ->
                        ParticipantItem(
                            viewModel.room,
                            participant,
                            speakerView = true
                        )
                    }
                speakerAdapter.update(items)
            }
        }
    }


    override fun onClickListener() {
        binding.ivChat.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                .setTitle("Send Message")
                .setView(editText)
                .setPositiveButton("Send") { dialog, _ ->
                    viewModel.sendData(editText.text?.toString() ?: "")
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()
                .show()
        }

        binding.ivCancel.setOnClickListener { finish() }

        binding.ivMore.setOnClickListener {
            XPopup.Builder(this@LiveKitMeetConferenceActivity)
                .atView(binding.viewContent)
                .asCustom(DialogMeetOptionMenu(
                    context = this,
                    listMenu = viewModel.mutableListOptionMenu.value ?: arrayListOf()
                ) {
                    when (it.menuName) {
                        OPTION_MEET_CLICK.MENU_SHARE_SCREEN.menuName -> {
                            if (viewModel.screenshareEnabled.value == true) {
                                viewModel.stopScreenCapture()
                            } else {
                                requestMediaProjection()
                            }
                        }

                        OPTION_MEET_CLICK.MENU_SOUND.menuName -> {
                            showSelectAudioDeviceDialog(viewModel)
                        }

                        OPTION_MEET_CLICK.MENU_PERMISSION.menuName -> {
                            viewModel.toggleSubscriptionPermissions()
                        }

                        OPTION_MEET_CLICK.MENU_DEBUG.menuName -> {
                            showDebugMenuDialog(viewModel)
                        }
                    }
                })
                .show()
        }

        binding.ivFlipCamera.setOnClickListener {
            viewModel.flipCamera()
        }

        binding.ivAudioSound.setOnClickListener {
            Toast.makeText(
                this@LiveKitMeetConferenceActivity,
                "Audio",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.ivInvitePerson.setOnClickListener {
            Toast.makeText(
                this@LiveKitMeetConferenceActivity,
                "Invite",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.ivArrowDown.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.error.collect {
                if (it != null) {
                    Toast.makeText(
                        this@LiveKitMeetConferenceActivity,
                        "Error: $it",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.dismissError()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.dataReceived.collect {
                Toast.makeText(
                    this@LiveKitMeetConferenceActivity,
                    "Data received: $it",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun requestMediaProjection() {
        val mediaProjectionManager =
            getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        screenCaptureIntentLauncher.launch(mediaProjectionManager.createScreenCaptureIntent())
    }

    override fun onDestroy() {
        binding.audienceRow.adapter = null
        binding.speakerView.adapter = null
        super.onDestroy()
    }

    override fun initViewModel() {
//        viewModel.screenshareEnabled.observe(this) { enabled ->
//            binding.screenShare.setImageResource(
//                if (enabled) {
//                    R.drawable.baseline_cast_connected_24
//                } else {
//                    R.drawable.baseline_cast_24
//                },
//            )
//        }
//
//        binding.enhancedNs.setOnClickListener {
//            showAudioProcessorSwitchDialog(viewModel)
//        }

        // Controls setup
        viewModel.cameraEnabled.observe(this) { enabled ->
            binding.ivVideo.setOnClickListener { viewModel.setCameraEnabled(!enabled) }
            binding.ivVideo.setImageResource(
                if (enabled) {
                    R.drawable.outline_videocam_24
                } else {
                    R.drawable.outline_videocam_off_24
                },
            )
            binding.ivFlipCamera.isEnabled = enabled
        }
        viewModel.micEnabled.observe(this) { enabled ->
            binding.ivMicrophone.setOnClickListener { viewModel.setMicEnabled(!enabled) }
            binding.ivMicrophone.setImageResource(
                if (enabled) {
                    R.drawable.outline_mic_24
                } else {
                    R.drawable.outline_mic_off_24
                },
            )
        }
//        viewModel.enhancedNsEnabled.observe(this) { enabled ->
//            binding.enhancedNs.visibility = if (enabled) {
//                android.view.View.VISIBLE
//            } else {
//                android.view.View.GONE
//            }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.permissionAllowed.collect { allowed ->
//                val resource =
//                    if (allowed) R.drawable.account_cancel_outline else R.drawable.account_cancel
//                binding.permissions.setImageResource(resource)
//            }
//        }
    }

    companion object {
        const val KEY_INPUT_ROOM_NAME = "roomName"
    }
}