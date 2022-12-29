package com.glebalekseevjk.yasmrhomework.ui

import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.glebalekseevjk.yasmrhomework.data.local.model.TodoItemDbModel
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.activity.MainActivity
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener



// activity_main.xml
@BindingAdapter("onNavigationItemSelectedListener")
fun bindNavigationItemSelectedListener(navigationView: NavigationView,block: OnNavigationItemSelectedListener) {
    navigationView.setNavigationItemSelectedListener {
        block.onNavigationItemSelected(it)
    }
}

// fragment_auth.xml
@BindingAdapter("loginAsText")
fun bindLoginAsText(textView: TextView, text: String){
    textView.text = "@$text"
}

// fragment_todo
@BindingAdapter("importanceAsText")
fun bindImportanceAsText(textView: TextView, importance: TodoItem.Companion.Importance) {
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
fun bindImportanceAsText(textView: TextView, date: Long?) {
    textView.text = date?.toString().orEmpty()
}
