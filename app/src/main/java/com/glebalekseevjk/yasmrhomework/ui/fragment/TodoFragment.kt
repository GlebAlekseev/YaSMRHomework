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
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.DAY_MILLIS
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.PLUG
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState.Companion.OK
import com.glebalekseevjk.yasmrhomework.ui.activity.MainActivity
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import com.glebalekseevjk.yasmrhomework.ui.listener.TodoOnScrollChangeListener
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import com.glebalekseevjk.yasmrhomework.utils.appComponent
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding: FragmentTodoBinding
        get() = _binding ?: throw RuntimeException("FragmentTodoBinding is null")

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var todoViewModel: TodoViewModel

    private lateinit var navController: NavController
    private lateinit var importancePopupMenu: PopupMenu

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createTodoFragmentSubComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todoViewModel = ViewModelProvider(this, viewModelFactory)[TodoViewModel::class.java]
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
        initNavigationUI()
        initImportancePopupMenu()
        initListeners()
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

    private fun initNavigationUI() {
        navController = findNavController()
    }

    private fun initListeners() {
        binding.exitClickListener = View.OnClickListener {
            activity?.onBackPressed()
        }
        binding.saveClickListener = View.OnClickListener {
            if (todoViewModel.currentState.screenMode == TodoViewModel.MODE_EDIT) {
                todoViewModel.editTodo(todoViewModel.currentState.todoItem) { result ->
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
            } else if (todoViewModel.currentState.screenMode == TodoViewModel.MODE_ADD) {
                todoViewModel.addTodo(todoViewModel.currentState.todoItem) { result ->
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
            if (todoViewModel.currentState.screenMode == TodoViewModel.MODE_EDIT) {
                todoViewModel.deleteTodo(todoViewModel.currentState.todoItem, {
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
            importancePopupMenu.show()
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
        if (!todoViewModel.currentState.isAuth) {
            navController.navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }
    private fun initImportancePopupMenu() {
        importancePopupMenu = PopupMenu(context, binding.importantLl)
        importancePopupMenu.inflate(R.menu.popup_menu)
        importancePopupMenu.setOnMenuItemClickListener {
            val importance = when (it.toString()) {
                "Нет" -> Importance.LOW
                "Низкий" -> Importance.BASIC
                "Высокий" -> Importance.IMPORTANT
                else -> throw RuntimeException("Importance $it is bad")
            }
            todoViewModel.updateState {
                it.copy(
                    todoItem = it.todoItem.copy(importance = importance)
                )
            }
            true
        }
    }

    companion object {
        const val TODO_ID = "todo_id"
        const val SCREEN_MODE = "screen_mode"
    }
}