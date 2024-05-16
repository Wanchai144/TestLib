package com.tdg.vroom.view.login

import androidx.activity.viewModels
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseActivity
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.databinding.ActivityLoginBinding
import com.tdg.vroom.ext.launchActivity
import com.tdg.vroom.ext.onTextChangeListener
import com.tdg.vroom.ext.requestNeededPermissionsCalendar
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.setVisiblePassword
import com.tdg.vroom.ext.startIntentWebView
import com.tdg.vroom.view.home.HomeActivity
import com.tdg.vroom.view.home.HomeVIewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    private val homeViewModel: HomeVIewModel by viewModels()

    private var isVisiblePassword = false
    override fun initView() {
        this.requestNeededPermissionsCalendar {
            localeHelper.setLocale(context = this, homeViewModel.getPreferenceApp(keyPreference = PreferenceUtility.LANGUAGE_KEY))
            setFullScreenStatusBarColor(colorStatusBar = R.color.gph_white)
            setLoadingView(viewModel.loadingState)
            onTextChangeListener()
        }
    }

    override fun onClickListener() = with(binding) {
        edtEmail.setText(homeViewModel.getPreferenceApp(keyPreference = PreferenceUtility.EMAIL))

        ivShowPassword.setOnClickListener {
            edtPassword.setVisiblePassword(
                isVisiblePassword = isVisiblePassword,
                ivShowPassword = ivShowPassword
            )
            isVisiblePassword = isVisiblePassword.not()
        }

        ivHome.setOnClickListener {
            this@LoginActivity.launchActivity<HomeActivity>()
        }

        btnLogin.setOnClickWithDebounce {
            viewModel.requestLogin(
                userName = edtEmail.text.toString(),
                password = edtPassword.text.toString()
            )
        }

        tvRegister.setOnClickListener {
            this@LoginActivity.startIntentWebView(url = "${BuildConfig.BASE_URL_REGISTER}=${localeHelper.getCurrentLocale()}")
        }

        tvForGotPassword.setOnClickListener {
            this@LoginActivity.startIntentWebView(url = "${BuildConfig.BASE_FORGOT_PASSWORD}=${localeHelper.getCurrentLocale()}")
        }
    }

    private fun onTextChangeListener() = with(binding) {
        edtEmail.onTextChangeListener { email ->
            isStateButtonLogin(email = email, password = edtPassword.text.toString())
        }

        edtPassword.onTextChangeListener { password ->
            isStateButtonLogin(email = edtEmail.text.toString(), password = password)
        }
    }

    private fun isStateButtonLogin(email: String, password: String) {
        binding.btnLogin.isEnabled = viewModel.isStateButtonLogin(
            email = email,
            password = password
        )
    }

    override fun initViewModel() {
        observeLogin()
        observeError()
    }

    private fun observeError() {
        viewModel.onErrorMessage.observe(this) {
            dialogMessage(
                title = resources.getString(R.string.dialog_title),
                message = it ?: ""
            ) {}
        }
    }

    private fun observeLogin() {
        viewModel.loginResponse.observe(this) {
            this@LoginActivity.launchActivity<HomeActivity>()
        }
    }

}