package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.rv.adapter.TaskListAdapter
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeController
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeControllerActions


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}