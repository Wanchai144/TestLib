package com.example.testlibs.persentation.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.example.testlibs.R
import com.example.testlibs.utils.getBinding
import qiu.niorgai.StatusBarCompat

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    private var loadingDialog: Dialog? = null

    private var loadingDialogConference: Dialog? = null

    abstract fun initView()

    abstract fun onClickListener()

    abstract fun initViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::binding.isInitialized.not()) {
            binding = getBinding()
            setContentView(binding.root)
            initView()
            onClickListener()
            initViewModel()
        }
    }


    fun onSetStatusBar(colorStatusBar: Boolean) {
        if (colorStatusBar) StatusBarCompat.setStatusBarColor(
            this, ContextCompat.getColor(
                this, R.color.colorPressed
            )
        )
        else {
            StatusBarCompat.translucentStatusBar(this, true)
            window.statusBarColor = Color.parseColor("#1A000000")
        }
    }

    fun setFullScreenStatusBarColor(colorStatusBar: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, colorStatusBar)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }

    fun setupFullScreen() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun setupFullScreenSetPaddingBottomXML() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    private fun startLoading() {
        loadingDialog?.show()
    }

    private fun stopLoading() {
        loadingDialog?.dismiss()
    }

    private fun startLoadingConference() {
        loadingDialogConference?.show()
    }

    private fun stopLoadingConference() {
        loadingDialogConference?.dismiss()
    }
}

