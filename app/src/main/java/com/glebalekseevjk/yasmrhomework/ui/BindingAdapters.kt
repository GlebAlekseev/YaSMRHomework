package com.glebalekseevjk.yasmrhomework.ui

import android.R
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance.*
import com.glebalekseevjk.yasmrhomework.utils.getFormattedDateFromTimestamp
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener


// activity_main.xml
@BindingAdapter("onNavigationItemSelectedListener")
fun bindNavigationItemSelectedListener(
    navigationView: NavigationView,
    block: OnNavigationItemSelectedListener
) {
    navigationView.setNavigationItemSelectedListener {
        block.onNavigationItemSelected(it)
    }
}

// fragment_auth.xml
@BindingAdapter("loginAsText")
fun bindLoginAsText(textView: TextView, text: String) {
    textView.text = "@$text"
}

// fragment_todo
@BindingAdapter("importanceAsSpinner")
fun bindImportanceAsSpinner(view: Spinner, importance: TodoItem.Companion.Importance) {
    val position = when(importance){
        LOW -> 0
        BASIC -> 1
        IMPORTANT -> 2
    }
    view.setSelection(position)
}

// fragment_todo_list
@BindingAdapter("strikeThrough")
fun bindImportanceAsText(checkBox: CheckBox, value: Boolean) {
    if (value) {
        checkBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        checkBox.paintFlags = 1283
    }
}

@BindingAdapter("dateAsText")
fun bindImportanceAsText(textView: TextView, timestamp: Long?) {
    if (timestamp == null) {
        textView.text = ""
    } else {
        textView.text = getFormattedDateFromTimestamp(timestamp, "d MMMM yyyy")
    }
}