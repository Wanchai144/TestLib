package com.example.testlibs.persentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.example.testlibs.R
import com.example.testlibs.databinding.ActivityChatBinding
import com.example.testlibs.utils.FragmentUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity  : BaseActivity<ActivityChatBinding>() {
    override fun initView() {
        onSetStatusBar(true)
    }

    override fun onClickListener() {

    }

    override fun initViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            val fragmentManager: FragmentManager = supportFragmentManager
            val chatFragment = ChatFragment()
            FragmentUtils.showFragmentWithAnimation(this, fragmentManager, chatFragment, R.id.fragment_container)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressed()
        val fragmentCount = supportFragmentManager.backStackEntryCount
        if (fragmentCount >1){
            supportFragmentManager.popBackStack()
        }else{
            lifecycleScope.launch {
                delay(100)
                finish()
//                overrideActivityTransition(R.anim.no_change,R.anim.slide_down)
            }
        }
    }
}

