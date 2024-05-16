package com.tdg.vroom.base

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.tdg.vroom.ext.buildLoading
import com.tdg.vroom.ext.getBinding
import com.tdg.vroom.utils.dialog.DialogUtils
import com.tdg.vroom.utils.livedata.SingleEvent
import javax.inject.Inject

abstract class BaseFragment<V : ViewBinding> : Fragment() {

    private var _binding: V? = null

    val binding: V
        get() = _binding
            ?: throw RuntimeException("Should only use binding after onCreateView and before onDestroyView")

    private var loadingDialog: Dialog? = null

    protected fun requireBinding(): V = requireNotNull(_binding) // final override

    abstract fun initView()

    abstract fun onClickListener()

    abstract fun initViewModel()

    @Inject
    lateinit var dialogUtils: DialogUtils

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClickListener()
        initViewModel()
        loadingDialog = requireContext().buildLoading()
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


    fun dialogMessage(title: String, message: String, callBack: (Boolean?) -> Unit?) {
        dialogUtils.dialogMessage(
            mTitle = title,
            mDescription = message,
            appCompatActivity = requireActivity() as AppCompatActivity
        ) {
            callBack.invoke(true)
        }
    }

    fun dialogMessageButton(title: String, message: String, callBack: (Boolean?) -> Unit?) {
        dialogUtils.dialogMessageAction(
            mTitle = title,
            mDescription = message,
            appCompatActivity = requireActivity() as AppCompatActivity
        ) {
            callBack.invoke(true)
        }
    }

    fun setFullScreenStatusBarColor(colorStatusBar: Int) {
        val window: Window? = activity?.window
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = ContextCompat.getColor(requireContext(), colorStatusBar)
        window?.let {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }
    }

    fun setupFullScreen() {
        activity?.window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun setupFullScreenSetPaddingBottomXML() {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }
    fun startLoading() {
        loadingDialog?.show()
    }

    fun stopLoading() {
        loadingDialog?.dismiss()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
