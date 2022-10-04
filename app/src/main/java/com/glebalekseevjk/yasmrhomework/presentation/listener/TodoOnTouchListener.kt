package com.glebalekseevjk.yasmrhomework.presentation.listener

import android.view.View
import android.widget.LinearLayout

class TodoOnScrollChangeListener(private val headerLl: LinearLayout) :
    View.OnScrollChangeListener {
    private var isScrollYZero = true

    override fun onScrollChange(view: View?, p1: Int, offsetY: Int, p3: Int, p4: Int) {
        // Если расширено, то добавить тень, иначе убрать для headerLl
        if (offsetY <= 5 && isScrollYZero == false) {
            isScrollYZero = true
            headerLl.elevation = 0f
        } else if (offsetY > 5 && isScrollYZero == true) {
            isScrollYZero = false
            headerLl.elevation = 15f
        }
    }
}