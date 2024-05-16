package com.tdg.vroom.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.tdg.vroom.ext.buildLoading
import com.tdg.vroom.ext.buildLoadingConference
import com.tdg.vroom.ext.getBinding
import com.tdg.vroom.ext.gone
import com.tdg.vroom.ext.visible
import com.tdg.vroom.utils.dialog.DialogUtils
import com.tdg.vroom.utils.livedata.SingleEvent
import com.tdg.vroom.utils.locale.LocaleHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    private var loadingDialog: Dialog? = null

    private var loadingDialogConference: Dialog? = null

    abstract fun initView()

    abstract fun onClickListener()

    abstract fun initViewModel()

    @Inject
    lateinit var localeHelper: LocaleHelper

    @Inject
    lateinit var dialogUtils: DialogUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::binding.isInitialized.not()) {
            binding = getBinding()
            setContentView(binding.root)
            localeHelper.initialize(this, localeHelper.getCurrentLocale())
            initView()
            onClickListener()
            initViewModel()
            loadingDialog = this.buildLoading()
            loadingDialogConference = this.buildLoadingConference()
        }
    }

    fun setLoadingView(stateLoading: LiveData<SingleEvent<Boolean>>) {
        stateLoading.observe(this) {
            it.consume()?.let { isLoading ->
                if (isLoading) {
                    startLoading()
                } else {
                    stopLoading()
                }
            }
        }
    }

    fun setLoadingViewConference(stateLoading: LiveData<SingleEvent<Boolean>>) {
        stateLoading.observe(this) {
            it.consume()?.let { isLoading ->
                if (isLoading) {
                    startLoadingConference()
                } else {
                    stopLoadingConference()
                }
            }
        }
    }

    fun dialogMessage(title: String, message: String, callBack: (Boolean?) -> Unit?) {
        dialogUtils.dialogMessage(
            mTitle = title,
            mDescription = message,
            appCompatActivity = this
        ) {
            callBack.invoke(true)
        }
    }

    fun dialogMessageButton(title: String, message: String, callBack: (Boolean?) -> Unit?) {
        dialogUtils.dialogMessageAction(
            mTitle = title,
            mDescription = message,
            appCompatActivity = this
        ) {
            callBack.invoke(true)
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

    fun updateLanguageApp(language: String) {
        localeHelper.setLocale(context = this, language = language) { refreshPage ->
            if (refreshPage) {
                recreate()
            }
        }
    }
}
