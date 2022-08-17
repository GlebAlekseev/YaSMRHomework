package com.glebalekseevjk.yasmrhomework.presentation.rv.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.presentation.activity.MainActivity
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoFragment
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoListFragment

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskItemViewHolder>() {
    var editClickListener: ((id: String)->Unit)? = null
    var taskList = listOf<TodoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item_task,
            parent,
            false
        )
        return TaskItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val todoItem = taskList[position]

        if (todoItem.finished){
            holder.checkBox.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.label_disable))
            holder.checkBox.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.checkBox.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.label_primary))
            holder.checkBox.paintFlags = 1283
        }

        holder.checkBox.text = todoItem.text
        holder.checkBox.isChecked = todoItem.finished

        holder.imageView.setOnClickListener{
            // Запустить TodoFragment MODE_EDIT
            editClickListener?.invoke(todoItem.id)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        val imageView: ImageView = view.findViewById(R.id.imageButton)
    }


}