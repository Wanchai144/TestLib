package com.example.testlibs.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testlibs.R

object FragmentUtils {
    fun showFragmentWithAnimation(
        context: Context,
        fragmentManager: FragmentManager,
        fragment: Fragment,
        containerId: Int
    ) {
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.slide_in_up,  // enter animation
            R.anim.fade_out,     // exit animation
            R.anim.fade_in,      // pop enter animation
            R.anim.slide_out_down // pop exit animation
        )
        fragmentTransaction.replace(containerId, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}