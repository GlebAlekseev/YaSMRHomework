package com.glebalekseevjk.yasmrhomework.presentation.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.presentation.listener.TodoOnScrollChangeListener

class TodoFragment : Fragment() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
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
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        exitIv.setOnClickListener {
            requireActivity().onBackPressed()
        }
        saveBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        removeLl.setOnClickListener {
            requireActivity().onBackPressed()
        }

        contentSv.setOnScrollChangeListener(TodoOnScrollChangeListener(
            headerLl
        ))
        importantLl.setOnClickListener {
            // Открыть Popup
            setPopupMenu()
        }
        deadlineSw.setOnClickListener {
            if ((it as Switch).isChecked) {
                deadlineDataTv.visibility = View.VISIBLE
            } else {
                deadlineDataTv.visibility = View.INVISIBLE
            }
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