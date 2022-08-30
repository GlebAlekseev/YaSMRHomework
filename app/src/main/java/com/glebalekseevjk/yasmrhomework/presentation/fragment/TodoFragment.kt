package com.glebalekseevjk.yasmrhomework.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.DEFAULT
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem.Companion.Importance
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import com.glebalekseevjk.yasmrhomework.presentation.listener.TodoOnScrollChangeListener
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel
import java.time.LocalDateTime

class TodoFragment : Fragment() {
    private val mainViewModel by lazy {
        ViewModelProvider(this,(context?.applicationContext as MainApplication).mainViewModelFactory).get(MainViewModel::class.java)
    }
    private var screenMode: String = MODE_ADD
    private var todoId: String = UNKNOWN_TODO_ID

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState == null){
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
        initViews(view)
        initListeners()
        initData()
    }

    private fun setTextMessageView(){
        messageEt.text = mainViewModel.currentTodoItem.text
    }

    private fun setImportanceView(){
        importantStateTv.text = when (mainViewModel.currentTodoItem.importance) {
            Importance.LOW -> {
                "Нет"
            }
            Importance.NORMAL -> {
                "Низкий"
            }
            Importance.URGENT -> {
                "Высокий"
            }
        }
    }

    private fun setDeadlineView(){
        if (mainViewModel.currentTodoItem.deadline != null){
            deadlineSw.isChecked = true
            deadlineDataTv.text = mainViewModel.currentTodoItem.deadline.toString()
            deadlineDataTv.visibility = View.VISIBLE
        }else{
            deadlineSw.isChecked = false
            deadlineDataTv.visibility = View.INVISIBLE
        }
    }

    private fun setRemoveButtonView(){
        if (screenMode == MODE_EDIT){
            removeLl.visibility = View.VISIBLE
        }else if(screenMode == MODE_ADD){
            removeLl.visibility = View.GONE
        }
    }

    private fun initData(){
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
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        exitIv.setOnClickListener {
            requireActivity().onBackPressed()
        }
        saveBtn.setOnClickListener {
            if (screenMode == MODE_EDIT){
                mainViewModel.editTodo(mainViewModel.currentTodoItem)
            }else if(screenMode == MODE_ADD){
                mainViewModel.addTodo(mainViewModel.currentTodoItem)
            }
            requireActivity().onBackPressed()
        }
        removeLl.setOnClickListener {
            if (screenMode == MODE_EDIT) {
                mainViewModel.deleteTodo(mainViewModel.currentTodoItem.id)
            }
            requireActivity().onBackPressed()
        }
        messageEt.addTextChangedListener {
            mainViewModel.currentTodoItem =
                mainViewModel.currentTodoItem.copy(text = it.toString())
        }

        contentSv.setOnScrollChangeListener(TodoOnScrollChangeListener(
            headerLl
        ))
        importantLl.setOnClickListener {
            setPopupMenu()
        }
        deadlineSw.setOnClickListener {
            if ((it as Switch).isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mainViewModel.currentTodoItem =
                        mainViewModel.currentTodoItem.copy(
                            deadline = LocalDateTime.now().plusDays(1)
                        )
                }
            } else {
                mainViewModel.currentTodoItem =
                    mainViewModel.currentTodoItem.copy(
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
            val importance = when(it.toString()){
                "Нет" -> Importance.LOW
                "Низкий" -> Importance.NORMAL
                "Высокий" -> Importance.URGENT
                else -> throw RuntimeException("Importance $it is bad")
            }
            mainViewModel.currentTodoItem =
                mainViewModel.currentTodoItem.copy(importance = importance)
            setImportanceView()
            true
        }
        popup.show()
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) throw RuntimeException("Param screen mode is absent")
        screenMode = args.getString(SCREEN_MODE).toString()
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) throw RuntimeException("Unknown screen mode $screenMode")
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(TODO_ID)) throw RuntimeException("Param todo id is absent")
            todoId = args.getString(TODO_ID).toString()
            mainViewModel.currentTodoItem = mainViewModel.getTodo(todoId)!!
        }else{
            mainViewModel.currentTodoItem = DEFAULT
        }
    }

    companion object {
        private const val TODO_ID = "todo_id"
        private const val SCREEN_MODE = "screen_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val UNKNOWN_TODO_ID = "0"

        fun newInstanceAddTodo(): TodoFragment {
            return TodoFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditTodo(todoId: String): TodoFragment {
            return TodoFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putString(TODO_ID, todoId)
                }
            }
        }
    }

}