package com.tdg.vroom.view.home.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.data.local.preferences.SharePreferenceUtility
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val sharePreferenceUtility: SharePreferenceUtility
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    fun getStateAudioMuted(): Boolean {
        return sharePreferenceUtility.getBoolean(PreferenceUtility.SWITCH_AUDIO_MUTED, false)
    }

    fun saveStateAudioMuted(stateOpen: Boolean) {
        sharePreferenceUtility.putBoolean(PreferenceUtility.SWITCH_AUDIO_MUTED, stateOpen)
    }

    fun getStateVideoMuted(): Boolean {
        return sharePreferenceUtility.getBoolean(PreferenceUtility.SWITCH_VIDEO_MUTED, false)
    }

    fun saveStateVideoMuted(stateOpen: Boolean) {
        sharePreferenceUtility.putBoolean(PreferenceUtility.SWITCH_VIDEO_MUTED, stateOpen)
    }

    fun getStateBatterySaving(): Boolean {
        return sharePreferenceUtility.getBoolean(PreferenceUtility.SWITCH_BATTERY_SAVING, false)
    }

    fun saveStateBatterySaving(stateOpen: Boolean) {
        sharePreferenceUtility.putBoolean(PreferenceUtility.SWITCH_BATTERY_SAVING, stateOpen)
    }

    fun getEmail(): String {
        return sharePreferenceUtility.getString(PreferenceUtility.EMAIL, "")
    }

    fun saveEmail(email: String) {
        sharePreferenceUtility.putString(PreferenceUtility.EMAIL, email)
    }
}