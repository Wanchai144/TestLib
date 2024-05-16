package com.tdg.vroom.view.home.ui.settings

import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseActivity
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.databinding.ActivitySettingsBinding
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.startIntentWebView
import com.tdg.vroom.view.home.HomeVIewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    private val viewModel: SettingsViewModel by viewModels()

    private val homeViewMode: HomeVIewModel by viewModels()
    override fun initView() {
        setFullScreenStatusBarColor(colorStatusBar = R.color.colorPrimary)
        bindText()
    }

    private fun bindText() = with(binding) {
        edtDisplayName.setText(homeViewMode.getDisplayName())
        edtEmail.setText(viewModel.getEmail())
        bindSwitchMode()
        tvVersion.text = BuildConfig.VERSION_NAME
        if (homeViewMode.getPreferenceApp(keyPreference = PreferenceUtility.ACCEPT_TOKEN).isEmpty()){
            edtEmail.setTextColor(ContextCompat.getColor(this@SettingsActivity,R.color.colorBaseText))
        }else{
            edtEmail.setTextColor(ContextCompat.getColor(this@SettingsActivity,R.color.colorShapeCircleLanguage))
        }
        edtEmail.isEnabled =
            homeViewMode.getPreferenceApp(keyPreference = PreferenceUtility.ACCEPT_TOKEN).isEmpty()
    }

    private fun bindSwitchMode() = with(binding) {
        switchAudio.isChecked = viewModel.getStateAudioMuted()
        switchVideo.isChecked = viewModel.getStateVideoMuted()
        switchBattery.isChecked = viewModel.getStateBatterySaving()
    }

    override fun onClickListener() = with(binding) {
        ivHome.setOnClickWithDebounce {
            homeViewMode.saveDisplayName(displayName = binding.edtDisplayName.text.toString())
            if (homeViewMode.getPreferenceApp(keyPreference = PreferenceUtility.ACCEPT_TOKEN).isEmpty()){
                homeViewMode.saveEmail(email = binding.edtEmail.text.toString())
            }
            finish()
        }
        switchAudio.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.saveStateAudioMuted(stateOpen = b)
        }

        switchVideo.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.saveStateVideoMuted(stateOpen = b)
        }

        switchBattery.setOnCheckedChangeListener { compoundButton, b ->
            viewModel.saveStateBatterySaving(stateOpen = b)
        }

        ivLinkPrivacy.setOnClickWithDebounce {
            this@SettingsActivity.startIntentWebView(url = BuildConfig.BASE_URL_POLICY)
        }
        ivLinkTerm.setOnClickWithDebounce {
            this@SettingsActivity.startIntentWebView(url = BuildConfig.BASE_URL_TERM_CONDITION)
        }
        onClickBackPressedDispatcher()
    }

    private fun onClickBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                homeViewMode.saveDisplayName(displayName = binding.edtDisplayName.text.toString())
                if (homeViewMode.getPreferenceApp(keyPreference = PreferenceUtility.ACCEPT_TOKEN).isEmpty()){
                    homeViewMode.saveEmail(email = binding.edtEmail.text.toString())
                }
                finish()
            }
        })
    }

    override fun initViewModel() {

    }
}