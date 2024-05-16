package com.tdg.vroom.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseViewModel
import com.tdg.vroom.data.local.ConstLanguage
import com.tdg.vroom.data.local.ConstMeetingConference
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.data.local.preferences.SharePreferenceUtility
import com.tdg.vroom.data.model.LanguageModel
import com.tdg.vroom.data.model.response.CheckVersionResponse
import com.tdg.vroom.data.remote.NetworkErrorException
import com.tdg.vroom.data.repository.GeneralRepository
import com.tdg.vroom.ext.getTextTwoCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeVIewModel @Inject constructor(
    private val sharePreferenceUtility: SharePreferenceUtility,
    private val generalRepository: GeneralRepository
) : BaseViewModel() {

    private val _languageList = MutableLiveData<ArrayList<LanguageModel>>()
    val languageList: LiveData<ArrayList<LanguageModel>> get() = _languageList

    private val _navigateLiveKit = MutableLiveData<Pair<Boolean,String>?>()
    val navigateLiveKit: LiveData<Pair<Boolean,String>?> get() = _navigateLiveKit

    private val _onErrorMessage = MutableLiveData<String>()
    val onErrorMessage: LiveData<String?> get() = _onErrorMessage

    fun requestCheckVersionMeeting(
        roomName: String
    ) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val response = generalRepository.checkVersionMeeting(
                roomName = roomName
            )
            withContext(Dispatchers.Main) {
                hideLoading()
                NetworkErrorException.onErrorException(
                    request = response,
                    onSuccess = {
                        _navigateLiveKit.postValue(Pair(checkVersionMeetingConference(response),roomName))
                    }
                ) {
                    _onErrorMessage.postValue(it)
                }
            }
        }
    }

    private fun checkVersionMeetingConference(response: Response<CheckVersionResponse>): Boolean {
        val xVersion = response.headers()[ConstMeetingConference.headerXVersionFiled]
        return xVersion == ConstMeetingConference.filedCheckVersionNavigate
    }

    fun requestLanguageList() {
        val listLanguage = ArrayList<LanguageModel>()
        val listName = arrayListOf(
            ConstLanguage.language_thai,
            ConstLanguage.language_english,
            ConstLanguage.language_china
        )
        val listIcon = arrayListOf(R.drawable.ic_thai, R.drawable.ic_eng, R.drawable.ic_china)

        listName.forEachIndexed { index, s ->
            listLanguage.add(
                LanguageModel(
                    name = listName[index],
                    code = listName[index],
                    icon = listIcon[index],
                    active = getLanguageActive(listName[index])
                )
            )
        }

        _languageList.postValue(listLanguage)
    }

    private fun getLanguageActive(language: String): Boolean {
        return sharePreferenceUtility.getString(
            PreferenceUtility.LANGUAGE_KEY,
            ConstLanguage.language_thai
        ) == language
    }

    fun updateLanguage(language: String) {
        sharePreferenceUtility.putString(PreferenceUtility.LANGUAGE_KEY, language)
    }

    fun getDisplayName(): String {
        return sharePreferenceUtility.getString(PreferenceUtility.FULL_NAME, "Gust")
    }

    fun saveDisplayName(displayName: String) {
        if (displayName != getDisplayName())
            sharePreferenceUtility.putString(PreferenceUtility.FULL_NAME, displayName)
    }

    fun saveEmail(email: String) {
        sharePreferenceUtility.putString(PreferenceUtility.EMAIL, email)
    }

    fun getProfileDisplayName(): String {
        val fullName = sharePreferenceUtility.getString(PreferenceUtility.FULL_NAME, "Gust")
        return fullName.getTextTwoCharacter()
    }

    fun getPreferenceApp(keyPreference: String): String {
        return sharePreferenceUtility.getString(keyPreference, "")
    }

    fun getPreferenceAppBoolean(keyPreference: String): Boolean {
        return sharePreferenceUtility.getBoolean(keyPreference, false)
    }

    fun savePreferenceAppBoolean(keyPreference: String, state: Boolean) {
        sharePreferenceUtility.putBoolean(keyPreference, state)
    }

}