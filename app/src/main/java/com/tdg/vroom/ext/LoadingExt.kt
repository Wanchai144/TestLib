package com.tdg.vroom.ext

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import com.tdg.vroom.R

fun Context.buildLoading(): Dialog {
    return Dialog(this).apply {
        setContentView(R.layout.loading_progress)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        setCancelable(false)

    }
}

fun Context.buildLoadingConference(): Dialog {
    return Dialog(this).apply {
        setContentView(R.layout.loading_conference)
        window?.run {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        setCancelable(false)

    }
}