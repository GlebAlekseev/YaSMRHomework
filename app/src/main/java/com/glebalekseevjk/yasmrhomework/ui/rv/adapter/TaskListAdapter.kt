package com.glebalekseevjk.yasmrhomework.ui.rv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.databinding.TaskItemRvBinding
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.rv.SwipeControllerActions
import com.glebalekseevjk.yasmrhomework.ui.rv.callback.TodoItemDiffCallBack
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoListViewModel

class TaskListAdapter :
    ListAdapter<TodoItem, TaskListAdapter.TaskItemViewHolder>(TodoItemDiffCallBack()) {
    var editClickListener: ((id: Long) -> Unit)? = null
    var todoListViewModel: TodoListViewModel? = null
    var swipeControllerActions: SwipeControllerActions? = null

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

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
        binding.todoListViewModel = todoListViewModel
            ?: throw RuntimeException("todoListViewModel is not setup for TaskListAdapter")
        binding.editClickListener = View.OnClickListener {
            // Запустить TodoFragment MODE_EDIT
            editClickListener?.invoke(todoItem.id)
        }
        binding.swipeControllerActions = swipeControllerActions
    }

    inner class TaskItemViewHolder(val binding: TaskItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        const val VIEW_TYPE = 1
    }
}