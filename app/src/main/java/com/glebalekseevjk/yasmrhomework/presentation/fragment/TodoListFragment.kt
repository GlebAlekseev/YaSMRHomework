package com.glebalekseevjk.yasmrhomework.presentation.fragment

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.repositoryImpl.TodoItemsRepositoryImpl
import com.glebalekseevjk.yasmrhomework.presentation.rv.adapter.TaskListAdapter
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeController
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeControllerActions
import com.glebalekseevjk.yasmrhomework.presentation.rv.listener.OnTouchListener
import com.glebalekseevjk.yasmrhomework.presentation.rv.listener.OnTouchListener.Companion.TouchEventSettings
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoListFragment : Fragment() {
    private lateinit var headerLl: LinearLayout
    private lateinit var headerCountTv: TextView
    private lateinit var taskListRv: RecyclerView
    private lateinit var addTaskBtn: FloatingActionButton

    private val dp: Float by lazy { resources.displayMetrics.density }
    private var mainViewModel: MainViewModel = MainViewModel()
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()
        initDispatchTouchEventSettings()
        setupRecyclerView()
    }

    private fun initViews(view: View) {
        with(view) {
            headerLl = findViewById(R.id.header_ll)
            headerCountTv = findViewById(R.id.header_count_tv)
            taskListRv = findViewById(R.id.task_list_rv)
            addTaskBtn = findViewById(R.id.add_task_btn)
        }
    }

    private fun initListeners() {
        addTaskBtn.setOnClickListener {
            // Запустить TodoFragment ADD_EDIT
            val fragment = TodoFragment.newInstanceAddTodo()
            launchFragment(fragment)
        }
    }

    private fun setupRecyclerView() {
        taskListAdapter = TaskListAdapter()
        with(taskListRv) {
            adapter = taskListAdapter
            mainViewModel.getTodoList().observeForever {
                headerCountTv.text = String.format(resources.getString(R.string.count_done), it.size)
                taskListAdapter.taskList = it
            }
            val swipeController = SwipeController(object : SwipeControllerActions() {
                override fun onLeftClicked(position: Int) {
                    // Завершение
                    mainViewModel.finishTodo(taskListAdapter.taskList[position])
                    Log.d("MainActivity", "finished on position $position")
                }
                override fun onRightClicked(position: Int) {
                    // Удаление
                    mainViewModel.deleteTodo(taskListAdapter.taskList[position])
                    Log.d("MainActivity", "removed on position $position")
                }
            })
            ItemTouchHelper(swipeController).attachToRecyclerView(this)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeController.onDraw(c)
                }
            })
            setOnTouchListener(OnTouchListener(
                headerLl,
                headerCountTv,
                dp))
            taskListAdapter.editClickListener = { id ->
                val fragment = TodoFragment.newInstanceEditTodo(todoId = id)
                launchFragment(fragment)
            }
        }

    }

    private fun initDispatchTouchEventSettings() {
        TouchEventSettings.maxPaddingTop = (90 * dp).toInt() + 1
        TouchEventSettings.minPaddingTop = (15 * dp).toInt()
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .addToBackStack(null)
            .commit()
    }
}