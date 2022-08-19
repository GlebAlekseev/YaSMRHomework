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

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.TaskItemViewHolder>() {
    var editClickListener: ((id: String)->Unit)? = null
    var taskList = listOf<TodoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.task_item_rv,
            parent,
            false
        )
        return TaskItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val todoItem = taskList[position]

        if (todoItem.finished){
            holder.statusCb.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.label_disable))
            holder.statusCb.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            holder.statusCb.setTextColor(ContextCompat.getColor(holder.itemView.context,R.color.label_primary))
            holder.statusCb.paintFlags = 1283
        }

        holder.statusCb.text = todoItem.text
        holder.statusCb.isChecked = todoItem.finished

        holder.infoIv.setOnClickListener{
            // Запустить TodoFragment MODE_EDIT
            editClickListener?.invoke(todoItem.id)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statusCb: CheckBox = view.findViewById(R.id.status_cb)
        val infoIv: ImageView = view.findViewById(R.id.info_iv)
    }


}