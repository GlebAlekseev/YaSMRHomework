package com.glebalekseevjk.yasmrhomework.presentation.fragment

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.Result
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.state.TodoListViewState
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import com.glebalekseevjk.yasmrhomework.presentation.rv.adapter.TaskListAdapter
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeController
import com.glebalekseevjk.yasmrhomework.presentation.rv.callback.SwipeControllerActions
import com.glebalekseevjk.yasmrhomework.presentation.rv.listener.OnTouchListener
import com.glebalekseevjk.yasmrhomework.presentation.rv.listener.OnTouchListener.Companion.TouchEventSettings
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoListViewModel
import com.glebalekseevjk.yasmrhomework.utils.observe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoListFragment : Fragment() {
    private val todoListViewModel by lazy {
        ViewModelProvider(this,(context?.applicationContext as MainApplication).todoListViewModelFactory)[TodoListViewModel::class.java]
    }
    private lateinit var headerLl: LinearLayout
    private lateinit var headerCountTv: TextView
    private lateinit var taskListRv: RecyclerView
    private lateinit var addTaskBtn: FloatingActionButton
    private lateinit var headerViewIv: ImageView

    private val dp: Float by lazy { resources.displayMetrics.density }
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
        initErrorHandler()
        initViews(view)
        initListeners()
        initDispatchTouchEventSettings()
        setupRecyclerView()
        observeViewModel()
    }
    private fun initErrorHandler(){
        lifecycleScope.launch{
            todoListViewModel.errorHandler.collect{
                if (it != -1) {
                    Toast.makeText(context,resources.getString(it), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun observeViewModel(){
        observeSubmitListAdapter()
    }

    private fun observeSubmitListAdapter(){
        lifecycleScope.launch {
            with(todoListViewModel){
                todoListViewState
                    .combine(isViewFinished){ state, isViewFinished ->
                        Pair(state,isViewFinished)
                    }
                    .collect{ (state, isViewFinished) ->
                        submitListAdapter(state, isViewFinished)
                    }
            }
        }
    }

    private fun submitListAdapter(state: TodoListViewState, isViewFinished: Boolean){
            when(state.result.status ){
                ResultStatus.SUCCESS -> {
                    val list = state.result.data
                    val newTaskList = if (isViewFinished) list.filter { !it.finished } else list
                    headerCountTv.text = String.format(resources.getString(R.string.count_done),
                        list.size - newTaskList.size)
                    taskListAdapter.submitList(newTaskList.toList())
                }
                ResultStatus.LOADING -> {
                    println("LOADING...")
                }
                ResultStatus.FAILURE -> {
                    println("ERROR: ${state.errorMessage}.")
                }
            }
    }

    private fun initViews(view: View) {
        with(view) {
            headerLl = findViewById(R.id.header_ll)
            headerCountTv = findViewById(R.id.header_count_tv)
            taskListRv = findViewById(R.id.task_list_rv)
            addTaskBtn = findViewById(R.id.add_task_btn)
            headerViewIv = findViewById(R.id.header_view_iv)
        }
    }

    private fun initListeners() {
        addTaskBtn.setOnClickListener {
            // Запустить TodoFragment ADD_EDIT
            val fragment = TodoFragment.newInstanceAddTodo()
            launchFragment(fragment)
        }
        headerViewIv.setOnClickListener{
            todoListViewModel.isViewFinished.value = !todoListViewModel.isViewFinished.value
        }

    }

    private fun setupRecyclerView() {
        taskListAdapter = TaskListAdapter()
        with(taskListRv) {
            adapter = taskListAdapter
            val swipeController = SwipeController(object : SwipeControllerActions() {
                override fun onLeftClicked(position: Int) {
                    todoListViewModel.finishTodo(taskListAdapter.currentList[position])
                }
                override fun onRightClicked(position: Int) {
                    todoListViewModel.deleteTodo(taskListAdapter.currentList[position])
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