package com.glebalekseevjk.yasmrhomework.ui

import android.R
import android.graphics.Paint
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
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
@BindingAdapter("importanceAsText")
fun bindImportanceAsText(textView: TextView, importance: TodoItem.Companion.Importance) {
    println("***************88 Bind importanceAsText")
    textView.text = when (importance) {
        TodoItem.Companion.Importance.LOW -> {
            "Нет"
        }
        TodoItem.Companion.Importance.BASIC -> {
            "Низкий"
        }
        TodoItem.Companion.Importance.IMPORTANT -> {
            "Высокий"
        }
    }
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

@BindingAdapter("isShowRemove")
fun bindIsShowRemove(view: View, isShow: Boolean) {
    if (isShow) {
        view.alpha = 1f
    } else {
        view.alpha = 0.2f
    }
    val typedValueColor = TypedValue()
    view.context.theme.resolveAttribute(R.attr.colorError, typedValueColor, true)
    if (isShow) {
        when (view) {
            is TextView -> {
                view.setTextColor(typedValueColor.data)
            }
            is AppCompatImageView -> {
                view.setColorFilter(typedValueColor.data)
            }
            else -> {}
        }
    }
}