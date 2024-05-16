package com.example.testlibs

import android.app.Activity
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testlibs.databinding.ActivityLivekitMeetBinding
import com.example.testlibs.persentation.adapter.ChatAdapter
import com.example.testlibs.persentation.ui.BaseActivity
import com.example.testlibs.persentation.ui.ChatActivity
import com.example.testlibs.persentation.ui.LiveKitMeetConferenceViewModel
import com.example.testlibs.persentation.ui.LiveKitMeetConferenceViewModelFactory
import com.example.testlibs.utils.DialogMeetOptionMenu
import com.example.testlibs.utils.gone
import com.example.testlibs.utils.requestNeededPermissions
import com.example.testlibs.utils.visible
import com.lxj.xpopup.XPopup
import com.xwray.groupie.GroupieAdapter
import kotlinx.coroutines.flow.collectLatest


class LiveKitActivity : BaseActivity<ActivityLivekitMeetBinding>() {
    private val viewModel: LiveKitMeetConferenceViewModel by viewModels {
        LiveKitMeetConferenceViewModelFactory(this)
    }

    private val chatMessages = mutableListOf<String>()
    private lateinit var chatAdapter: ChatAdapter

    private val screenCaptureIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode != RESULT_OK || data == null) {
                return@registerForActivityResult
            }
//            viewModel.requestConferenceLiveKit(
//                url = "wss://livekit-dev.truevirtualworld.com",
//                roomName = intent?.getStringExtra(KEY_INPUT_ROOM_NAME).toString()
//            )
            viewModel.startScreenCapture(data)
        }

    override fun initView() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestNeededPermissions {
            setUpMeetConference()
        }
        viewModel.setUpConferenceMeet("wss://testlive-mff4cv72.livekit.cloud")
    }

    override fun onClickListener() {
        binding.ivChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        binding.ivMore.setOnClickListener {
            XPopup.Builder(this)
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

                        OPTION_MEET_CLICK.MENU_RAISE_HAND.menuName -> {
                             viewModel.toggleRaiseHand()
                        }
                    }
                })
                .show()
        }

        binding.ivCancel.setOnClickListener { finish() }
    }

    override fun initViewModel() {
        viewModel.cameraEnabled.observe(this) { enabled ->
            binding.ivVideo.setOnClickListener { viewModel.setCameraEnabled(!enabled) }
            binding.ivVideo.setImageResource(
                if (enabled) {
                    R.drawable.outline_videocam_24
                } else {
                    R.drawable.outline_videocam_off_24
                },
            )
//            binding.ivFlipCamera.isEnabled = enabled
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
    }


    private fun setUpMeetConference() {
//        binding.tvRoomName.text = intent.getStringExtra(KEY_INPUT_ROOM_NAME)
        // Audience row setup
        val audienceAdapter = GroupieAdapter()
        binding.audienceRow.apply {
            layoutManager = LinearLayoutManager(
                this@LiveKitActivity,
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
                this@LiveKitActivity,
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


    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.error.collect {
                if (it != null) {
                    Toast.makeText(
                        this@LiveKitActivity,
                        "Error: $it",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.dismissError()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.dataReceived.collect { pair ->
                val (identity, message) = pair
                when {
                    message.contains("raise_hand") -> {
                        val fullIdentity = "Identity(value=aaa)"
                        val regex = "Identity\\(value=(.+)\\)".toRegex()
                        val identity = regex.find(fullIdentity)?.groupValues?.get(1) ?: "Unknown"
                        binding.tvRaiseHand.visible()
                        binding.tvRaiseHand.text = "$identity would like to speak."
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.tvRaiseHand.gone()
                        }, 3000)
                    }
                    message.contains("lower_hand") -> {
                        binding.tvRaiseHand.gone()
                    }
                    else -> {
                        // จัดการกับข้อความอื่นๆ

                    }
                }
            }
        }
    }

    private fun sendMessage() {
//        val message = binding.messageEditText.text.toString()
//        if (message.isNotEmpty()) {
//            viewModel.sendData(message)
//            chatMessages.add(message)
//            chatAdapter.notifyDataSetChanged()
//            binding.messageEditText.text.clear()
//        }
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

    companion object {
        const val KEY_INPUT_ROOM_NAME = "roomName"
    }
}