package com.tdg.vroom.view.login

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tdg.vroom.base.BaseViewModel
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.data.local.preferences.SharePreferenceUtility
import com.tdg.vroom.data.model.request.LoginBody
import com.tdg.vroom.data.model.response.LoginResponse
import com.tdg.vroom.data.model.response.ModelPackage
import com.tdg.vroom.data.model.response.ModelProfile
import com.tdg.vroom.data.model.response.PackageName
import com.tdg.vroom.data.remote.NetworkErrorException
import com.tdg.vroom.data.repository.GeneralRepository
import com.tdg.vroom.utils.JWTUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val sharePreferenceUtility: SharePreferenceUtility,
    val generalRepository: GeneralRepository
) : BaseViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse?>()
    val loginResponse: LiveData<LoginResponse?> get() = _loginResponse

    private val _onErrorMessage = MutableLiveData<String>()
    val onErrorMessage: LiveData<String?> get() = _onErrorMessage

    fun isStateButtonLogin(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }

    fun isLogin(): Boolean {
        return sharePreferenceUtility.getBoolean(PreferenceUtility.FLAG_LOGIN, false)
    }

    fun requestLogin(
        userName: String,
        password: String
    ) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val response = generalRepository.onLogin(
                loginBody = LoginBody(
                    grantType = "password",
                    scope = "openid profile email",
                    username = userName,
                    password = password
                )
            )
            withContext(Dispatchers.Main) {
                hideLoading()
                NetworkErrorException.onErrorException(
                    request = response,
                    onSuccess = {
                        val responseBody = response.body()
                        if (responseBody?.accessToken?.isNotEmpty() == true) {
                            val profileLogin =
                                JWTUtils.decoded<ModelProfile>(responseBody.idToken ?: "")
                            saveProfileLogin(responseBody, profileLogin)
                            requestGetPackageName(responseBody)
                        }
                    }
                ) {
                    _onErrorMessage.postValue(it)
                }
            }
        }
    }

    @SuppressLint("LogNotTimber")
    private fun requestGetPackageName(
        responseBody: LoginResponse?
    ) {
        showLoading()
        CoroutineScope(Dispatchers.IO).launch {
            val response = generalRepository.getPackageName(
                token = responseBody?.idToken ?: ""
            )
            withContext(Dispatchers.Main) {
                hideLoading()
                NetworkErrorException.onErrorException(
                    request = response,
                    onSuccess = {
                        val responseBodyPackage = response.body()
                        if (responseBodyPackage?.token?.isNotEmpty() == true) {
                            val profilePackage = JWTUtils.decoded<ModelPackage>(responseBodyPackage.token)
                            val packageName = JWTUtils.decodedPackage<PackageName>(profilePackage?.context?.user?.p?:"")
                            savePackName(profilePackage, packageName)
                            _loginResponse.postValue(responseBody)
                        }
                    }
                ) {
                    Log.e(LoginViewModel::class.java.simpleName,"call package name : error : $it")
                    _loginResponse.postValue(responseBody)
                }
            }
        }
    }

    private fun savePackName(profilePackage: ModelPackage?, packageName: PackageName?) {
        sharePreferenceUtility.apply {
            putString(PreferenceUtility.PROFILE_AVATAR, profilePackage?.context?.user?.avatar?:"")
            putString(PreferenceUtility.PACKAGE_NAME, packageName?.packageNameEN?:"")
            putString(PreferenceUtility.FULL_NAME, packageName?.displayName?:"")
            putString(PreferenceUtility.PACKAGE_CODE, packageName?.packageCode?:"")
        }
    }

    fun saveProfileLogin(response: LoginResponse?, profileLogin: ModelProfile?) {
        sharePreferenceUtility.apply {
            putBoolean(PreferenceUtility.FLAG_LOGIN, response?.accessToken?.isNotEmpty() == true)
            putString(PreferenceUtility.ACCEPT_TOKEN, response?.accessToken ?: "")
            putString(PreferenceUtility.ID_TOKEN, response?.idToken ?: "")
            putString(PreferenceUtility.EMAIL, profileLogin?.preferredUsername ?: "")
            putString(PreferenceUtility.FULL_NAME, profileLogin?.name ?: "")
        }
    }

    fun clearProfileLogin() {
        sharePreferenceUtility.apply {
            remove(PreferenceUtility.FLAG_LOGIN)
            remove(PreferenceUtility.ACCEPT_TOKEN)
            remove(PreferenceUtility.ID_TOKEN)
            remove(PreferenceUtility.FULL_NAME)
            remove(PreferenceUtility.PROFILE_AVATAR)
            remove(PreferenceUtility.PACKAGE_NAME)
            remove(PreferenceUtility.PACKAGE_CODE)
        }
    }
}