package com.tdg.vroom.view.home

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.tdg.vroom.BuildConfig
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseActivity
import com.tdg.vroom.data.local.preferences.PreferenceUtility
import com.tdg.vroom.databinding.ActivityHomeBinding
import com.tdg.vroom.ext.clearKeyboardFocus
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.launchActivity
import com.tdg.vroom.ext.load
import com.tdg.vroom.ext.setOnClickWithDebounce
import com.tdg.vroom.ext.startIntentWebView
import com.tdg.vroom.ext.visible
import com.tdg.vroom.view.adapter.LanguageAdapter
import com.tdg.vroom.view.home.ui.menuRecent.RecentViewModel
import com.tdg.vroom.view.home.ui.settings.SettingsActivity
import com.tdg.vroom.view.login.LoginActivity
import com.tdg.vroom.view.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val homeViewModel: HomeVIewModel by viewModels()

    private val loginViewModel: LoginViewModel by viewModels()

    private val languageAdapter: LanguageAdapter by lazy { LanguageAdapter() }

    private var isStateCaptureOn = false

    private var isStateMicrophoneOn = false

    override fun initView() {
        setFullScreenStatusBarColor(colorStatusBar = R.color.colorPrimary)
        initNavigatorMenu()
        initListLanguage()
    }

    private fun bindControllMeeting() = with(binding) {
        isStateCaptureOn =
            homeViewModel.getPreferenceAppBoolean(keyPreference = PreferenceUtility.SWITCH_VIDEO_MUTED)
        isStateMicrophoneOn =
            homeViewModel.getPreferenceAppBoolean(keyPreference = PreferenceUtility.SWITCH_AUDIO_MUTED)

        if (isStateMicrophoneOn) {
            ivMic.setImageResource(R.drawable.ic_mic_on)
        } else {
            ivMic.setImageResource(R.drawable.ic_mic_off)
        }


        if (isStateCaptureOn) {
            ivCapture.setImageResource(R.drawable.ic_cam_on)
        } else {
            ivCapture.setImageResource(R.drawable.ic_cam_off)
        }
    }

    override fun onClickListener() {
        binding.apply {
            icNavBottom.btnLoginOrRegister.setOnClickWithDebounce {
                loginViewModel.clearProfileLogin()
                this@HomeActivity.launchActivity<LoginActivity>()
                finish()
            }
            onClickToolbarHome()
            onClickNavigatorMenu()
        }

        languageAdapter.onItemClick = {
            homeViewModel.updateLanguage(language = it.code)
            updateLanguageApp(language = it.code)
        }
        onClickBackPressedDispatcher()
    }

    private fun onClickNavigatorMenu() = with(binding) {
        ivNavMenu.setOnClickListener {
            binding.drawerLayout.clearKeyboardFocus()
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener {
            val id: Int = it.itemId
            when (id) {
                R.id.nav_plane -> {
                    this@HomeActivity.startIntentWebView(url = BuildConfig.BASE_URL_PRICING)
                    Handler(mainLooper).postDelayed({
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                    }, 550)
                }

                R.id.nav_settings -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    this@HomeActivity.launchActivity<SettingsActivity>()
                }
            }

            return@OnNavigationItemSelectedListener false
        })
    }

    private fun onClickToolbarHome() = with(binding) {
        ivMic.setOnClickListener {
            binding.drawerLayout.clearKeyboardFocus()
            if (isStateMicrophoneOn) {
                ivMic.setImageResource(R.drawable.ic_mic_off)
            } else {
                ivMic.setImageResource(R.drawable.ic_mic_on)
            }
            isStateMicrophoneOn = isStateMicrophoneOn.not()
            homeViewModel.savePreferenceAppBoolean(
                keyPreference = PreferenceUtility.SWITCH_AUDIO_MUTED,
                isStateMicrophoneOn
            )
        }

        ivCapture.setOnClickListener {
            binding.drawerLayout.clearKeyboardFocus()
            if (isStateCaptureOn) {
                ivCapture.setImageResource(R.drawable.ic_cam_off)
            } else {
                ivCapture.setImageResource(R.drawable.ic_cam_on)
            }
            isStateCaptureOn = isStateCaptureOn.not()
            homeViewModel.savePreferenceAppBoolean(
                keyPreference = PreferenceUtility.SWITCH_VIDEO_MUTED,
                isStateCaptureOn
            )
        }
    }

    private fun onClickBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun initListLanguage() = with(binding) {
        icNavBottom.rvLanguage.apply {
            adapter = languageAdapter
        }
    }

    private fun initNavigatorMenu() {

    }

    @SuppressLint("SetTextI18n")
    private fun bindTextNavigatorMenu() {
        binding.apply {
            val navigationView: NavigationView = binding.navView
            val header = navigationView.getHeaderView(0)
            val tvFullName = header?.findViewById<TextView>(R.id.tvFullName)
            val ivAvatar = header?.findViewById<ImageView>(R.id.ivAvatar)
            val viewPackageName = header?.findViewById<Group>(R.id.viewPackageName)
            val tvPackageName = header?.findViewById<TextView>(R.id.tvPackageName)

            tvFullName?.text = homeViewModel.getDisplayName()

            if (homeViewModel.getPreferenceApp(keyPreference = PreferenceUtility.ACCEPT_TOKEN)
                    .isNotEmpty()
            ) {
                icNavBottom.tvNavSingUp.gone()
                icNavBottom.btnLoginOrRegister.text = resources.getString(R.string.message_logout)
                ivAvatar?.load(homeViewModel.getPreferenceApp(keyPreference = PreferenceUtility.PROFILE_AVATAR))

                val packageName =
                    homeViewModel.getPreferenceApp(keyPreference = PreferenceUtility.PACKAGE_NAME)
                if (packageName.isNotEmpty()) {
                    viewPackageName?.visible()
                    tvPackageName?.text = packageName
                }
            }
        }
    }

    override fun initViewModel() {
        observeLanguage()
    }

    private fun observeLanguage() = with(homeViewModel) {
        requestLanguageList()
        languageList.observe(this@HomeActivity) {
            languageAdapter.submitList(it)
        }
    }

    override fun onResume() {
        super.onResume()
        bindTextNavigatorMenu()
        bindControllMeeting()
    }
}