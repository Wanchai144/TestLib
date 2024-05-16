package com.tdg.vroom.view.splash

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.tdg.vroom.R
import com.tdg.vroom.base.BaseActivity
import com.tdg.vroom.databinding.ActivitySplashScreenBinding
import com.tdg.vroom.ext.launchActivity
import com.tdg.vroom.view.home.HomeActivity
import com.tdg.vroom.view.home.ui.menuCalendar.CalendarViewModel
import com.tdg.vroom.view.login.LoginActivity
import com.tdg.vroom.view.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding>() {

    private val loginViewModel: LoginViewModel by viewModels()

    private val calendarViewModel: CalendarViewModel by viewModels()
    override fun initView() {
        setFullScreenStatusBarColor(colorStatusBar = R.color.gph_white)
        calendarViewModel.deleteCalendar()
        Handler(Looper.getMainLooper()).postDelayed({
            if (loginViewModel.isLogin()) {
                launchActivity<HomeActivity>()
            } else {
                launchActivity<LoginActivity>()
            }
        }, 1500)
    }

    override fun initViewModel() {

    }

    override fun onClickListener() {

    }
}