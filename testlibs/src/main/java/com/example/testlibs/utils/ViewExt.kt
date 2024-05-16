package com.example.testlibs.utils

import android.content.Context
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

//fun EditText.setVisiblePassword(isVisiblePassword: Boolean, ivShowPassword: ImageView) {
//    val edtPassword = this
//    if (isVisiblePassword) {
//        edtPassword.transformationMethod = PasswordTransformationMethod()
//        ivShowPassword.setBackgroundResource(R.drawable.eye_on)
//    } else {
//        edtPassword.transformationMethod = null
//        ivShowPassword.setBackgroundResource(R.drawable.eye_off)
//    }
//}

fun EditText.onTextChangeListener(textChange: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            textChange.invoke(p0.toString())
        }
    })
}

fun View.setOnClickWithDebounce(debounceTime: Long = 1000L, onClick: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime >= debounceTime) {
                onClick.invoke()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        }
    })
}

fun View.clearKeyboardFocus() {
    clearFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}
