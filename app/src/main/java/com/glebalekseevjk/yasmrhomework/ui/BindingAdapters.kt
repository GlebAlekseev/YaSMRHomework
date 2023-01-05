package com.glebalekseevjk.yasmrhomework.ui

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.yasmrhomework.R
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
    val position = when (importance) {
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

@BindingAdapter("isShowFinished", "listTodoItem")
fun bindCountDoneAsText(textView: TextView, isShowFinished: Boolean, listTodoItem: List<TodoItem>) {
    val list = if (!isShowFinished) listTodoItem.filter { !it.done } else listTodoItem

    textView.text = String.format(
        textView.resources.getString(R.string.count_done),
        list.size
    )
}

@BindingAdapter("isShowFinished")
fun bindIsShowFinishedDrawable(appCompatImageButton: AppCompatImageButton, isShowFinished: Boolean) {
    val drawable = if (isShowFinished) ResourcesCompat.getDrawable(appCompatImageButton.resources,R.drawable.ic_baseline_eye_24,appCompatImageButton.context.theme)
    else ResourcesCompat.getDrawable(appCompatImageButton.resources,R.drawable.ic_baseline_eyeoff_24, appCompatImageButton.context.theme)
    appCompatImageButton.setImageDrawable(drawable)
}