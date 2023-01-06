package com.glebalekseevjk.yasmrhomework.ui

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.utils.getFormattedDateFromTimestamp
import com.google.android.material.card.MaterialCardView
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
fun bindImportanceAsSpinner(view: Spinner, importance: Importance) {
    val position = when (importance) {
        Importance.LOW -> 0
        Importance.BASIC -> 1
        Importance.IMPORTANT -> 2
    }
    view.setSelection(position)
}

// fragment_todo_list
@BindingAdapter("isDone")
fun bindImAsText(textView: TextView, isDone: Boolean) {
    val colorOnPrimary = TypedValue()
    val colorOnTertiary = TypedValue()
    val theme = textView.context.theme
    theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, colorOnPrimary, true)
    theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary, colorOnTertiary, true)
    if (isDone) {
        textView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        textView.setTextColor(colorOnTertiary.data)
    } else {
        textView.paintFlags = 1283
        textView.setTextColor(colorOnPrimary.data)
    }
}

@BindingAdapter("importanceTypeAsText")
fun bindImportanceTypeAsText(textView: TextView, importance: Importance) {
    val colorError = TypedValue()
    val colorOnTertiary = TypedValue()
    val theme = textView.context.theme
    theme.resolveAttribute(com.google.android.material.R.attr.colorError, colorError, true)
    theme.resolveAttribute(com.google.android.material.R.attr.colorOnTertiary, colorOnTertiary, true)
    when(importance){
        Importance.LOW -> textView.visibility = View.GONE
        Importance.BASIC -> {
            textView.text = "↓"
            textView.setTextColor(colorOnTertiary.data)
            textView.visibility = View.VISIBLE
        }
        Importance.IMPORTANT -> {
            textView.text = "!!"
            textView.setTextColor(colorError.data)
            textView.visibility = View.VISIBLE
        }
    }
//    if (value) {
//        checkBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
//    } else {
//        checkBox.paintFlags = 1283
//    }
}

@BindingAdapter("dateAsText")
fun bindImrtanceAsText(textView: TextView, timestamp: Long?) {
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


@BindingAdapter("isDarkTheme")
fun bindIsDarkTheme(materialCardView: MaterialCardView, isDarkTheme: Boolean) {
    val colorOnPrimary = TypedValue()
    val theme = materialCardView.context.theme
    theme.resolveAttribute(com.google.android.material.R.attr.colorOnPrimary, colorOnPrimary, true)
    materialCardView.setCardBackgroundColor(colorOnPrimary.data)
    val delegate = (materialCardView.context as AppCompatActivity).delegate
    when(delegate.localNightMode){
       AppCompatDelegate.MODE_NIGHT_YES->{
           if (!isDarkTheme){
               delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
           }
       }
       AppCompatDelegate.MODE_NIGHT_NO->{
           if (isDarkTheme){
               delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
           }
       }
    }
}