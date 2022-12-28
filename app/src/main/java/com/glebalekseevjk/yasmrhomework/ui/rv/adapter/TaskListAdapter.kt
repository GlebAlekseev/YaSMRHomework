package com.glebalekseevjk.yasmrhomework.ui.rv.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.databinding.TaskItemRvBinding
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.rv.callback.TodoItemDiffCallBack

class TaskListAdapter :
    ListAdapter<TodoItem, TaskListAdapter.TaskItemViewHolder>(TodoItemDiffCallBack()) {
    var editClickListener: ((id: Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val binding = DataBindingUtil.inflate<TaskItemRvBinding>(
            LayoutInflater.from(parent.context),
            R.layout.task_item_rv,
            parent,
            false
        )
        return TaskItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val todoItem = getItem(position)
        val binding = holder.binding
        binding.todoItem = todoItem
        binding.editClickListener = View.OnClickListener {
            // Запустить TodoFragment MODE_EDIT
            editClickListener?.invoke(todoItem.id)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    inner class TaskItemViewHolder(val binding: TaskItemRvBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE = 1
    }
}