package com.glebalekseevjk.yasmrhomework.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.databinding.FragmentAuthBinding
import com.glebalekseevjk.yasmrhomework.databinding.FragmentTodoBinding
import com.glebalekseevjk.yasmrhomework.di.FromViewModelFactory
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.DAY_MILLIS
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.PLUG
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState.Companion.OK
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import com.glebalekseevjk.yasmrhomework.ui.listener.TodoOnScrollChangeListener
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import com.glebalekseevjk.yasmrhomework.utils.appComponent
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding: FragmentTodoBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoBinding is null")

    @FromViewModelFactory
    @Inject
    lateinit var todoViewModel: TodoViewModel

    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createTodoFragmentSubComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todo, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todoViewModel = todoViewModel
        initErrorHandler()
        initNavigationUI()
        initListeners()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        val screenMode = args.getString(SCREEN_MODE).toString()
        todoViewModel.setCurrentScreenMode(screenMode)

        if (screenMode == TodoViewModel.MODE_EDIT) {
            if (!args.containsKey(TODO_ID)) throw RuntimeException("Param todo id is absent")
            val todoId = args.getLong(TODO_ID)
            todoViewModel.setCurrentTodoItemById(todoId)
        }
    }

    private fun initErrorHandler() {
        lifecycleScope.launch {
            todoViewModel.errorHandler.collect {
                if (it != OK) {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initListeners() {
        binding.exitClickListener = View.OnClickListener {
            activity?.onBackPressed()
        }
        binding.saveClickListener = View.OnClickListener {
            if (todoViewModel.currentScreenMode.value == TodoViewModel.MODE_EDIT) {
                todoViewModel.editTodo(todoViewModel.currentTodoItem.value) { result ->
                    when (result.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupOneTimeCheckSynchronize()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else if (todoViewModel.currentScreenMode.value == TodoViewModel.MODE_ADD) {
                todoViewModel.addTodo(todoViewModel.currentTodoItem.value) { result ->
                    when (result.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupOneTimeCheckSynchronize()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            activity?.onBackPressed()
        }
        binding.removeClickListener = View.OnClickListener {
            if (todoViewModel.currentScreenMode.value == TodoViewModel.MODE_EDIT) {
                todoViewModel.deleteTodo(todoViewModel.currentTodoItem.value, {
                    false
                }) { result ->
                    when (result.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupOneTimeCheckSynchronize()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            activity?.onBackPressed()
        }
        binding.importantClickListener = View.OnClickListener {
            setPopupMenu()
        }
        binding.deadlineSwitchClickListener = View.OnClickListener {
            if ((it as SwitchCompat).isChecked) {
                todoViewModel.currentTodoItem.value =
                    todoViewModel.currentTodoItem.value.copy(
                        deadline = System.currentTimeMillis() + DAY_MILLIS,
                    )
            } else {
                todoViewModel.currentTodoItem.value =
                    todoViewModel.currentTodoItem.value.copy(
                        deadline = null
                    )
            }
            setDeadlineView()
        }
        binding.deadlineDateClickListener = View.OnClickListener {
            val datePickerDialog = DatePickerDialog(requireActivity())
            datePickerDialog.show()
        }

        binding.contentSv.setOnScrollChangeListener(
            TodoOnScrollChangeListener(
                binding.headerLl
            )
        )
    }

    private fun checkAuth() {
        if (!todoViewModel.isAuth) {
            navController.navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }

    private fun setPopupMenu() {
        val popup = PopupMenu(requireContext(), binding.importantLl)
        popup.inflate(R.menu.popup_menu)
        popup.setOnMenuItemClickListener {
            val importance = when (it.toString()) {
                "Нет" -> Importance.LOW
                "Низкий" -> Importance.BASIC
                "Высокий" -> Importance.IMPORTANT
                else -> throw RuntimeException("Importance $it is bad")
            }
            todoViewModel.currentTodoItem.value =
                todoViewModel.currentTodoItem.value.copy(importance = importance)
            true
        }
        popup.show()
    }

    private fun setDeadlineView() {
        if (todoViewModel.currentTodoItem.value.deadline != null) {
            binding.deadlineSw.isChecked = true
            binding.deadlineDateTv.text = todoViewModel.currentTodoItem.value.deadline?.toString().orEmpty()
            binding.deadlineDateTv.visibility = View.VISIBLE
        } else {
            binding.deadlineSw.isChecked = false
            binding.deadlineDateTv.visibility = View.INVISIBLE
        }
    }

    private fun initData() {
        setDeadlineView()
        setRemoveButtonView()
    }

    private fun setRemoveButtonView() {
        if (todoViewModel.currentScreenMode.value == TodoViewModel.MODE_EDIT) {
            binding.removeLl.visibility = View.VISIBLE
        } else if (todoViewModel.currentScreenMode.value == TodoViewModel.MODE_ADD) {
            binding.removeLl.visibility = View.GONE
        }
    }

























    companion object {
        const val TODO_ID = "todo_id"
        const val SCREEN_MODE = "screen_mode"
    }
}