package com.glebalekseevjk.yasmrhomework.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.databinding.FragmentTodoListBinding
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.ui.rv.SwipeControllerActions
import com.glebalekseevjk.yasmrhomework.ui.rv.adapter.TaskListAdapter
import com.glebalekseevjk.yasmrhomework.ui.rv.callback.SwipeCallback
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoListViewModel
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import com.glebalekseevjk.yasmrhomework.utils.appComponent
import com.google.android.material.shape.CornerFamily
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import kotlin.math.pow


class TodoListFragment : Fragment() {
    private var _binding: FragmentTodoListBinding? = null
    private val binding: FragmentTodoListBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoListBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoListViewModel: TodoListViewModel

    private lateinit var navController: NavController

    private lateinit var taskListAdapter: TaskListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createTodoListFragmentSubComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoListViewModel = ViewModelProvider(this, viewModelFactory)[TodoListViewModel::class.java]
        synchronizeTodoList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoListViewModel = todoListViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        initNavigationUI()
        initListeners()
        initAppBar()
        setupRecyclerView()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun synchronizeTodoList() {
        todoListViewModel.synchronizeTodoList { result ->
            when (result.status) {
                ResultStatus.FAILURE -> {
                    todoListViewModel.setupOneTimeCheckSynchronize()
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                ResultStatus.UNAUTHORIZED -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    checkAuth()
                }
                else -> {}
            }
        }
    }

    private fun checkAuth() {
        if (!todoListViewModel.currentState.isAuth) {
            navController.navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initListeners() {
        binding.addClickListener = View.OnClickListener {
            navigateToTodoFragmentWithAddMode()
        }

        binding.taskListSrl.setOnRefreshListener {
            todoListViewModel.synchronizeTodoList { result ->
                when (result.status) {
                    ResultStatus.SUCCESS -> {
                        binding.taskListSrl.isRefreshing = false
                        Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show()
                    }
                    ResultStatus.LOADING -> {
                    }
                    ResultStatus.FAILURE -> {
                        todoListViewModel.setupOneTimeCheckSynchronize()
                        binding.taskListSrl.isRefreshing = false
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                    ResultStatus.UNAUTHORIZED -> {
                        checkAuth()
                        binding.taskListSrl.isRefreshing = false
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initAppBar() {
        binding.appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val relativePosition =
                (appBarLayout.totalScrollRange + verticalOffset).toFloat() / appBarLayout.totalScrollRange
            val alpha = relativePosition.toDouble().pow(3.0).toFloat()
            binding.countDoneTv.alpha = if (alpha < 0.1) 0f else alpha

            val radius = resources.getDimension(R.dimen.spacing_small)
            binding.materialCardView.shapeAppearanceModel = if (relativePosition == 0f) {
                binding.appbar.elevation = resources.getDimension(R.dimen.elevation_large)
                binding.materialCardView.shapeAppearanceModel.toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, radius)
                    .setTopLeftCornerSize(0f)
                    .setTopRightCornerSize(0f)
                    .build()
            } else {
                binding.appbar.elevation = 0f
                binding.materialCardView.shapeAppearanceModel.toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, radius)
                    .build()
            }
        }
    }

    private fun setupRecyclerView() {
        taskListAdapter = TaskListAdapter()
        taskListAdapter.todoListViewModel = todoListViewModel
        with(binding.taskListRv) {
            adapter = taskListAdapter
            val swipeCallback = SwipeCallback(resources.getDimension(R.dimen.todolist_swipe_width))
            ItemTouchHelper(swipeCallback).attachToRecyclerView(this)

            taskListAdapter.swipeControllerActions = object : SwipeControllerActions() {
                override fun onLeftClicked(view: View, todoItem: TodoItem) {
                    swipeCallback.resetViewHolder((view.parent as View))
                    todoListViewModel.changeDoneTodo(todoItem) { result ->
                        when (result.status) {
                            ResultStatus.SUCCESS -> {
                            }
                            ResultStatus.LOADING -> {
                            }
                            ResultStatus.FAILURE -> {
                                todoListViewModel.setupOneTimeCheckSynchronize()
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                            ResultStatus.UNAUTHORIZED -> {
                                checkAuth()
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                override fun onRightClicked(view: View, todoItem: TodoItem) {
                    swipeCallback.resetViewHolder((view.parent as View))
                    todoListViewModel.deleteTodo(todoItem, {
                        val snackBar: Snackbar =
                            Snackbar.make(this@with, "Удаление через 3..", 3000)
                        var status = false
                        val mutex = Mutex(locked = true)
                        snackBar.setAction("Отменить") {
                            status = true
                            if (mutex.isLocked) {
                                mutex.unlock()
                            }
                        }
                        snackBar.show()
                        lifecycleScope.launch {
                            delay(1000)
                            snackBar.setText("Удаление через 2..")
                            delay(1000)
                            snackBar.setText("Удаление через 1..")
                            delay(1000)
                            if (mutex.isLocked) {
                                mutex.unlock()
                            }
                        }
                        mutex.withLock {
                            status
                        }
                    }) { result ->
                        when (result.status) {
                            ResultStatus.SUCCESS -> {
                            }
                            ResultStatus.LOADING -> {
                            }
                            ResultStatus.FAILURE -> {
                                todoListViewModel.setupOneTimeCheckSynchronize()
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                            ResultStatus.UNAUTHORIZED -> {
                                checkAuth()
                                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            taskListAdapter.editClickListener = { id ->
                navigateToTodoFragmentWithEditMode(id)
            }
        }
    }

    private fun navigateToTodoFragmentWithEditMode(todoId: Long){
        val action = TodoListFragmentDirections.actionTodoListFragmentToTodoFragment(todoId)
        navController.navigate(action)
    }

    private fun navigateToTodoFragmentWithAddMode(){
        navigateToTodoFragmentWithEditMode(TodoFragment.NONE)
    }

    private fun observeViewModel() {
        observeSubmitListAdapter()
    }

    private fun observeSubmitListAdapter() {
        lifecycleScope.launch {
            todoListViewModel.observeState(viewLifecycleOwner) {
                submitListAdapter(it.listTodoItem, it.isShowFinished)
            }
        }
    }

    private fun submitListAdapter(listTodoItem: List<TodoItem>, isShowFinished: Boolean) {
        val newTaskList = if (!isShowFinished) listTodoItem.filter { !it.done } else listTodoItem
        taskListAdapter.submitList(newTaskList.toList())
    }
}