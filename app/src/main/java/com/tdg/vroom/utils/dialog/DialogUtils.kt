package com.tdg.vroom.utils.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tdg.vroom.R
import com.tdg.vroom.ext.gone
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class DialogUtils @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun dialogMessageAction(
        mTitle: String,
        mDescription: String,
        appCompatActivity: AppCompatActivity,
        mCallback: () -> Unit
    ) {
        val dialog = Dialog(appCompatActivity)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inflater: LayoutInflater = appCompatActivity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.common_dialog, null)
        dialog.setContentView(dialogView)

        dialog.window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.82).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
        )

        dialog.apply {
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val tvDescription = findViewById<TextView>(R.id.tvDescription)
            val negativeButton = findViewById<Button>(R.id.negativeButton)
            val positiveButton = findViewById<Button>(R.id.positiveButton)
            tvTitle.text = mTitle
            tvDescription.text = mDescription

            negativeButton.apply {
                setOnClickListener {
                    dialog.cancel()
                }
            }

            positiveButton.apply {
                setOnClickListener {
                    mCallback.invoke()
                    dialog.cancel()
                }
            }
        }
        dialog.setCancelable(true)
        dialog.show()
    }

    fun dialogMessage(
        mTitle: String,
        mDescription: String,
        appCompatActivity: AppCompatActivity,
        mCallback: () -> Unit
    ) {
        val dialog = Dialog(appCompatActivity)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val inflater: LayoutInflater = appCompatActivity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.common_dialog, null)
        dialog.setContentView(dialogView)

        dialog.window?.setLayout(
            (context.resources.displayMetrics.widthPixels * 0.82).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT,
        )

        dialog.apply {
            val tvTitle = findViewById<TextView>(R.id.tvTitle)
            val tvDescription = findViewById<TextView>(R.id.tvDescription)
            val negativeButton = findViewById<Button>(R.id.negativeButton)
            val positiveButton = findViewById<Button>(R.id.positiveButton)
            negativeButton.gone()
            tvTitle.text = mTitle
            tvDescription.text = mDescription

            negativeButton.apply {
                setOnClickListener {
                    dialog.cancel()
                }
            }

            positiveButton.apply {
                setOnClickListener {
                    mCallback.invoke()
                    dialog.cancel()
                }
            }
        }
        dialog.setCancelable(true)
        dialog.show()
    }
}