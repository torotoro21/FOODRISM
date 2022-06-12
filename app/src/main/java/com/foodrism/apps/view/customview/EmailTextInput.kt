package com.foodrism.apps.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import com.foodrism.apps.R
import com.google.android.material.textfield.TextInputEditText

class EmailTextInput: TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(text: Editable) {
                if (!emailFormat(text)){
                    showError()
                }
            }
        })
    }

    private fun emailFormat(email: CharSequence): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showError() {
        error = context.getString(R.string.email_error)
    }
}