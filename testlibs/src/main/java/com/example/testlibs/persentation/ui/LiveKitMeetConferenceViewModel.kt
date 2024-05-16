package com.example.testlibs.persentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testlibs.data.repository.GeneralRepository
import com.example.testlibs.menuIcon
import com.example.testlibs.menuName
import com.example.testlibs.utils.NetworkErrorException
import io.livekit.android.AudioOptions
import io.livekit.android.LiveKit
import io.livekit.android.LiveKitOverrides
import io.livekit.android.RoomOptions
import io.livekit.android.audio.AudioProcessorOptions
import io.livekit.android.audio.AudioSwitchHandler
import io.livekit.android.events.RoomEvent
import io.livekit.android.events.collect
import io.livekit.android.room.Room
import io.livekit.android.room.participant.LocalParticipant
import io.livekit.android.room.participant.Participant
import io.livekit.android.room.participant.RemoteParticipant
import io.livekit.android.room.track.DataPublishReliability
import io.livekit.android.room.track.LocalScreencastVideoTrack
import io.livekit.android.util.flow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import livekit.LivekitModels

class LiveKitMeetConferenceViewModel(
    private val context: Context,
//                                      private val generalRepository: GeneralRepository,
//                                      private val sharePreferenceUtility: SharePreferenceUtility
) : BaseViewModel() {

    private val mutableWatermarkEnabled = MutableLiveData(false)
    val watermarkEnabled: LiveData<Boolean> = mutableWatermarkEnabled

    fun toggleWatermark() {
        mutableWatermarkEnabled.value =
            !(mutableWatermarkEnabled.value ?: false)
        applyWatermark(mutableWatermarkEnabled.value ?: false)
    }


    private fun applyWatermark(enable: Boolean) {

    }

    private fun getRoomOptions(): RoomOptions {
        return RoomOptions(
            adaptiveStream = true,
            dynacast = true,
        )
    }

    private var isHandRaised = false

    val audioProcessorOptions: AudioProcessorOptions? = null

    val room = LiveKit.create(
        appContext = context,
        options = getRoomOptions(),
        overrides = LiveKitOverrides(
            audioOptions = AudioOptions(audioProcessorOptions = audioProcessorOptions),
        ),
    )

    val audioHandler = room.audioHandler as AudioSwitchHandler

    val participants = room::remoteParticipants.flow
        .map { remoteParticipants ->
            listOf<Participant>(room.localParticipant) +
                remoteParticipants
                    .keys
                    .sortedBy { it.value }
                    .mapNotNull { remoteParticipants[it] }
        }
    private val mutableError = MutableStateFlow<Throwable?>(null)
    val error = mutableError.hide()

    private val mutablePrimarySpeaker = MutableStateFlow<Participant?>(null)
    val primarySpeaker: StateFlow<Participant?> = mutablePrimarySpeaker


    val activeSpeakers = room::activeSpeakers.flow

    private var localScreencastTrack: LocalScreencastVideoTrack? = null

    // Controls
    private val mutableMicEnabled = MutableLiveData(true)
    val micEnabled = mutableMicEnabled.hide()

    private val mutableCameraEnabled = MutableLiveData(true)
    val cameraEnabled = mutableCameraEnabled.hide()

    private val mutableScreencastEnabled = MutableLiveData(false)
    val screenshareEnabled = mutableScreencastEnabled.hide()

    private val mutableEnhancedNsEnabled = MutableLiveData(false)
    val enhancedNsEnabled = mutableEnhancedNsEnabled.hide()

    private val mutableEnableAudioProcessor = MutableLiveData(true)
    val enableAudioProcessor = mutableEnableAudioProcessor.hide()

    // Emits a string whenever a data message is received.
    private val mutableDataReceived = MutableSharedFlow<Pair<Any, String>>()
    val dataReceived = mutableDataReceived


    // Whether other participants are allowed to subscribe to this participant's tracks.
    private val mutablePermissionAllowed = MutableStateFlow(true)
    val permissionAllowed = mutablePermissionAllowed.hide()

    private var url = ""
    private var accessToken = ""
    private var roomName = ""

    private val _mutableListOptionMenu =
        MutableLiveData<ArrayList<MeetOptionMenuModel>>()
    val mutableListOptionMenu: LiveData<ArrayList<MeetOptionMenuModel>> get() = _mutableListOptionMenu

    init {
        seUpOptionMenu()
    }


    private fun seUpOptionMenu() {
        val listMenuName = ArrayList<MeetOptionMenuModel>()

        for (i in menuIcon.indices) {
            listMenuName.add(
                MeetOptionMenuModel(
                    menuName = menuName[i],
                    icon = menuIcon[i]
                )
            )
        }

        _mutableListOptionMenu.postValue(listMenuName)
    }

//    fun requestConferenceLiveKit(
//        url: String,
//        roomName: String
//    ) {
//        showLoading()
//        this.roomName = roomName
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = generalRepository.doGenerateTokenLiveKit(
//                identity = sharePreferenceUtility.getString(
//                    PreferenceUtility.FULL_NAME,
//                    "userTest"
//                ),
//                name = sharePreferenceUtility.getString(PreferenceUtility.FULL_NAME, "userTest"),
//                roomName = roomName
//            )
//            withContext(Dispatchers.Main) {
//                hideLoading()
//                NetworkErrorException.onErrorException(
//                    request = response!!,
//                    onSuccess = {
//                        this@LiveKitMeetConferenceViewModel.accessToken =
//                            response.body()?.accessToken ?: ""
//                        setUpConferenceMeet(url)
//                    }
//                ) {
//                    hideLoading()
////                    MyLog.e("call service error : ${it}")
//                }
//            }
//        }
//    }


    fun setUpConferenceMeet(url: String) {
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3MTU3NzE5MDYsImlzcyI6IkFQSWlVeUF2QlpaanltZCIsIm5iZiI6MTcxNTc3MTAwNiwic3ViIjoiV2FuY2hpYSIsInZpZGVvIjp7ImNhblB1Ymxpc2giOnRydWUsImNhblB1Ymxpc2hEYXRhIjp0cnVlLCJjYW5TdWJzY3JpYmUiOnRydWUsInJvb20iOiJUZXN0TGl2ZSIsInJvb21Kb2luIjp0cnVlfX0.FEkcYwc8q8loc3V-DBVl-enSH-u-xZbbEUlctyDc0Tw"
        viewModelScope.launch {
            // Collect any errors.
            launch {
                error.collect {
                }
            }

            // Handle any changes in speakers.
            launch {
                combine(
                    participants,
                    activeSpeakers
                ) { participants, speakers -> participants to speakers }
                    .collect { (participantsList, speakers) ->
                        handlePrimarySpeaker(
                            participantsList,
                            speakers,
                            room,
                        )
                    }
            }

            // Handle room events.
            launch {
                room.events.collect {
                    when (it) {
                        is RoomEvent.FailedToConnect -> mutableError.value =
                            it.error

                        is RoomEvent.DataReceived -> {
                            val identity = it.participant?.identity ?: "server"
                            val message = it.data.toString(Charsets.UTF_8)
                            val dataPair = Pair(identity, message)
                            mutableDataReceived.emit(dataPair)  // ส่ง Pair ไปยัง observer
                        }
                        else -> {

                        }
                    }
                }
            }
            connectToRoom(url = url, token = token)
        }
    }

    private suspend fun connectToRoom(url: String, token: String) {
//        this.url = url
//        this.accessToken = token

        try {
            room.connect(
                url = url,
                token = token,
            )


            mutableEnhancedNsEnabled.postValue(room.audioProcessorIsEnabled)
            mutableEnableAudioProcessor.postValue(true)

            // Create and publish audio/video tracks
            val localParticipant = room.localParticipant
            localParticipant.setMicrophoneEnabled(true)
            mutableMicEnabled.postValue(localParticipant.isMicrophoneEnabled())

            localParticipant.setCameraEnabled(true)
            mutableCameraEnabled.postValue(localParticipant.isCameraEnabled())

            // Update the speaker
            handlePrimarySpeaker(emptyList(), emptyList(), room)


        } catch (e: Throwable) {
            mutableError.value = e
        }
    }

    private fun handlePrimarySpeaker(
        participantsList: List<Participant>,
        speakers: List<Participant>,
        room: Room?
    ) {
        var speaker = mutablePrimarySpeaker.value

        // If speaker is local participant (due to defaults),
        // attempt to find another remote speaker to replace with.
        if (speaker is LocalParticipant) {
            val remoteSpeaker = participantsList
                .filterIsInstance<RemoteParticipant>() // Try not to display local participant as speaker.
                .firstOrNull()

            if (remoteSpeaker != null) {
                speaker = remoteSpeaker
            }
        }

        // If previous primary speaker leaves
        if (!participantsList.contains(speaker)) {
            // Default to another person in room, or local participant.
            speaker = participantsList.filterIsInstance<RemoteParticipant>()
                .firstOrNull()
                ?: room?.localParticipant
        }

        if (speakers.isNotEmpty() && !speakers.contains(speaker)) {
            val remoteSpeaker = speakers
                .filterIsInstance<RemoteParticipant>() // Try not to display local participant as speaker.
                .firstOrNull()

            if (remoteSpeaker != null) {
                speaker = remoteSpeaker
            }
        }

        mutablePrimarySpeaker.value = speaker
    }

    override fun onCleared() {
        super.onCleared()
        // Make sure to release any resources associated with LiveKit
        room.disconnect()
        room.release()
    }


    fun toggleRaiseHand() {
        viewModelScope.launch {
            room.localParticipant?.let { localParticipant ->
                isHandRaised = !isHandRaised
                val message = if (isHandRaised) "raise_hand" else "lower_hand"
                room.localParticipant.publishData(message.toByteArray(Charsets.UTF_8))
            }
        }
    }

    fun startScreenCapture(mediaProjectionPermissionResultData: Intent) {
        val localParticipant = room.localParticipant
        viewModelScope.launch {
            val screencastTrack =
                localParticipant.createScreencastTrack(
                    mediaProjectionPermissionResultData = mediaProjectionPermissionResultData
                )
            localParticipant.publishVideoTrack(
                screencastTrack,
            )

            // Must start the foreground prior to startCapture.
            screencastTrack.startForegroundService(null, null)
            screencastTrack.startCapture()

            this@LiveKitMeetConferenceViewModel.localScreencastTrack =
                screencastTrack
            mutableScreencastEnabled.postValue(screencastTrack.enabled)
        }
    }

    fun stopScreenCapture() {
        viewModelScope.launch {
            localScreencastTrack?.let { localScreencastVideoTrack ->
                localScreencastVideoTrack.stop()
                room.localParticipant.unpublishTrack(localScreencastVideoTrack)
                mutableScreencastEnabled.postValue(
                    localScreencastTrack?.enabled ?: false
                )
            }
        }
    }


    fun setMicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            room.localParticipant.setMicrophoneEnabled(enabled)
            mutableMicEnabled.postValue(enabled)
        }
    }

    fun setCameraEnabled(enabled: Boolean) {
        viewModelScope.launch {
            room.localParticipant.setCameraEnabled(enabled)
            mutableCameraEnabled.postValue(enabled)
        }
    }

    fun sendData(message: String) {
        viewModelScope.launch {
            room.localParticipant.publishData(message.toByteArray(Charsets.UTF_8))
        }
    }

    fun toggleSubscriptionPermissions() {
        mutablePermissionAllowed.value = !mutablePermissionAllowed.value
        room.localParticipant.setTrackSubscriptionPermissions(
            mutablePermissionAllowed.value
        )
    }

    // Debug functions
    fun simulateMigration() {
        room.sendSimulateScenario(Room.SimulateScenario.MIGRATION)
    }

    @SuppressLint("LogNotTimber")
    fun reconnect(url: String, token: String) {
        mutablePrimarySpeaker.value = null
        room.disconnect()
        viewModelScope.launch {
            connectToRoom(url = url, token = token)
        }
    }

    fun dismissError() {
        mutableError.value = null
    }

    private fun <T> LiveData<T>.hide(): LiveData<T> = this
    private fun <T> MutableStateFlow<T>.hide(): StateFlow<T> = this
    private fun <T> Flow<T>.hide(): Flow<T> = this

}