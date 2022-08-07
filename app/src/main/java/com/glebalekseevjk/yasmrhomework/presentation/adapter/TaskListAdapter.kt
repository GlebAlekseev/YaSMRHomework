package com.glebalekseevjk.yasmrhomework.presentation.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.TaskItem

class TaskListAdapter: RecyclerView.Adapter<TaskListAdapter.TaskItemViewHolder>() {


    val list = listOf<TaskItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 0
    }
    class TaskItemViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val imageButton = view.findViewById<ImageButton>(R.id.imageButton)
    }
}