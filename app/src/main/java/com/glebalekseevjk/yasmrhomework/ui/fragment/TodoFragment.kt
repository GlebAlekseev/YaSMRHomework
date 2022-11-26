package com.glebalekseevjk.yasmrhomework.ui.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.DAY_MILLIS
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.PLUG
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoListViewState.Companion.OK
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import com.glebalekseevjk.yasmrhomework.ui.listener.TodoOnScrollChangeListener
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.TodoViewModel
import kotlinx.coroutines.launch

class TodoFragment : Fragment() {
    private val mainApplication: MainApplication by lazy {
        requireContext().applicationContext as MainApplication
    }
    private val todoViewModel by lazy {
        ViewModelProvider(
            this,
            (context?.applicationContext as MainApplication).todoViewModelFactory
        )[TodoViewModel::class.java]
    }
    private var screenMode: String = MODE_ADD
    private var todoId: Long = TodoItem.UNDEFINED

    private lateinit var headerLl: LinearLayout
    private lateinit var contentLl: LinearLayout
    private lateinit var importantLl: LinearLayout
    private lateinit var exitIv: ImageView
    private lateinit var saveBtn: Button
    private lateinit var contentSv: ScrollView
    private lateinit var removeLl: LinearLayout
    private lateinit var deadlineSw: Switch
    private lateinit var deadlineDataTv: TextView
    private lateinit var messageEt: TextView
    private lateinit var importantStateTv: TextView

    private lateinit var navController: NavController

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
    ): View? {
        return inflater.inflate(R.layout.fragment_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initErrorHandler()
        initViews(view)
        initListeners()
        initData()
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

    private fun setTextMessageView() {
        messageEt.text = todoViewModel.currentTodoItem.text
    }

    private fun setImportanceView() {
        importantStateTv.text = when (todoViewModel.currentTodoItem.importance) {
            Importance.LOW -> {
                "Нет"
            }
            Importance.BASIC -> {
                "Низкий"
            }
            Importance.IMPORTANT -> {
                "Высокий"
            }
        }
    }

    private fun setDeadlineView() {
        if (todoViewModel.currentTodoItem.deadline != null) {
            deadlineSw.isChecked = true
            deadlineDataTv.text = todoViewModel.currentTodoItem.deadline.toString()
            deadlineDataTv.visibility = View.VISIBLE
        } else {
            deadlineSw.isChecked = false
            deadlineDataTv.visibility = View.INVISIBLE
        }
    }

    private fun setRemoveButtonView() {
        if (screenMode == MODE_EDIT) {
            removeLl.visibility = View.VISIBLE
        } else if (screenMode == MODE_ADD) {
            removeLl.visibility = View.GONE
        }
    }

    private fun initData() {
        setTextMessageView()
        setImportanceView()
        setDeadlineView()
        setRemoveButtonView()
    }

    private fun initViews(view: View) {
        with(view) {
            exitIv = findViewById(R.id.exit_iv)
            headerLl = findViewById(R.id.header_ll)
            contentLl = findViewById(R.id.content_ll)
            saveBtn = findViewById(R.id.save_btn)
            contentSv = findViewById(R.id.content_sv)
            removeLl = findViewById(R.id.remove_ll)
            importantLl = findViewById(R.id.important_ll)
            deadlineSw = findViewById(R.id.deadline_sw)
            deadlineDataTv = findViewById(R.id.deadline_date_tv)
            messageEt = findViewById(R.id.message_et)
            importantStateTv = findViewById(R.id.important_state_tv)
            navController = findNavController()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        exitIv.setOnClickListener {
            activity?.onBackPressed()
        }
        saveBtn.setOnClickListener {
            if (screenMode == MODE_EDIT) {
                todoViewModel.editTodo(todoViewModel.currentTodoItem) {
                    when (it.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupCheckSynchronizedWorker()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else if (screenMode == MODE_ADD) {
                todoViewModel.addTodo(todoViewModel.currentTodoItem) {
                    when (it.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupCheckSynchronizedWorker()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            activity?.onBackPressed()
        }
        removeLl.setOnClickListener {
            if (screenMode == MODE_EDIT) {
                todoViewModel.deleteTodo(todoViewModel.currentTodoItem, {
                    false
                }) {
                    when (it.status) {
                        ResultStatus.SUCCESS -> {
                        }
                        ResultStatus.LOADING -> {
                        }
                        ResultStatus.FAILURE -> {
                            todoViewModel.setupCheckSynchronizedWorker()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        ResultStatus.UNAUTHORIZED -> {
                            checkAuth()
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            activity?.onBackPressed()
        }
        messageEt.addTextChangedListener {
            todoViewModel.currentTodoItem =
                todoViewModel.currentTodoItem.copy(text = it.toString())
        }
        contentSv.setOnScrollChangeListener(
            TodoOnScrollChangeListener(
                headerLl
            )
        )
        importantLl.setOnClickListener {
            setPopupMenu()
        }
        deadlineSw.setOnClickListener {
            if ((it as Switch).isChecked) {
                todoViewModel.currentTodoItem =
                    todoViewModel.currentTodoItem.copy(
                        deadline = System.currentTimeMillis() + DAY_MILLIS,
                    )
            } else {
                todoViewModel.currentTodoItem =
                    todoViewModel.currentTodoItem.copy(
                        deadline = null
                    )
            }
            setDeadlineView()
        }
        deadlineDataTv.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireActivity())
            datePickerDialog.show()
        }

    }

    private fun setPopupMenu() {
        val popup = PopupMenu(requireActivity(), importantLl)
        popup.inflate(R.menu.popup_menu)
        popup.setOnMenuItemClickListener {
            val importance = when (it.toString()) {
                "Нет" -> Importance.LOW
                "Низкий" -> Importance.BASIC
                "Высокий" -> Importance.IMPORTANT
                else -> throw RuntimeException("Importance $it is bad")
            }
            todoViewModel.currentTodoItem =
                todoViewModel.currentTodoItem.copy(importance = importance)
            setImportanceView()
            true
        }
        popup.show()
    }

    private fun parseParams() {
        todoViewModel.currentTodoItem = PLUG
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        screenMode = args.getString(SCREEN_MODE).toString()
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) throw RuntimeException("Unknown screen mode $screenMode")
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(TODO_ID)) throw RuntimeException("Param todo id is absent")
            todoId = args.getLong(TODO_ID)
            todoViewModel.setCurrentTodoItemById(todoId)
        }
    }

    private fun checkAuth() {
        if (!todoViewModel.isAuth) {
            navController.navigate(R.id.action_todoListFragment_to_authFragment)
        }
    }

    companion object {
        const val TODO_ID = "todo_id"
        const val SCREEN_MODE = "screen_mode"
        const val MODE_ADD = "mode_add"
        const val MODE_EDIT = "mode_edit"
    }
}