package com.glebalekseevjk.yasmrhomework.data.repositoryImpl

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.glebalekseevjk.yasmrhomework.domain.entity.TodoItem
import com.glebalekseevjk.yasmrhomework.domain.repository.TodoItemsRepository
import java.lang.RuntimeException
import java.time.LocalDateTime

class TodoItemsRepositoryImpl: TodoItemsRepository {
    private var callbackListener: ((List<TodoItem>)->Unit)? = null
    private var todoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mutableListOf(
            TodoItem(
                id = "20",
                text = "Est fugiat explicabo nam obcaecati consequatur aut modi maxime est eveniet nihil non numquam error. ",
                importance = TodoItem.Companion.Importance.LOW,
                finished = false,
                created = LocalDateTime.now(),
                deadline = LocalDateTime.now().plusDays(1),
                edited = null
            ),
            TodoItem(
                id = "1",
                text = "Et expedita earum hic voluptatibus totam ad galisum nemo est eligendi dolor ut sapiente quod ea expedita magni. ",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = true,
                created = LocalDateTime.now(),
                deadline = LocalDateTime.now().plusDays(1),
                edited = null
            ),
            TodoItem(
                id = "2",
                text = "Est excepturi aspernatur hic quibusdam explicabo ut facere laboriosam rem architecto esse qui dolorem repudiandae in neque harum. ",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "3",
                text = " In quia reiciendis ab voluptates Quis non ipsam nesciunt id quaerat facere est sint corporis et minima illum.",
                importance = TodoItem.Companion.Importance.LOW,
                finished = true,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "4",
                text = "Est rerum dolores et voluptatem consequatur sit unde dolores est voluptas unde est magni inventore temporibus maiores. " +
                        "Qui voluptatem cumque sunt quasi vel autem molestias sit dolorum voluptate et similique aperiam est numquam possimus. ",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "5",
                text = "33 molestias neque quo quaerat saepe 33 deserunt delectus est nihil doloribus et laborum magnam aut omnis sequi ut consequuntur libero.",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "6",
                text = "Cum aliquam optio aut velit molestiae id amet voluptate et error harum est consectetur impedit. ",
                importance = TodoItem.Companion.Importance.LOW,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "7",
                text = "Ut deleniti nulla aut deleniti unde eos ipsam autem non repellendus quia.",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "8",
                text = "Vel repellat sunt aut dolorem perspiciatis qui corrupti quaerat qui dolores corporis.",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "9",
                text = "Aut voluptas cumque cum debitis facere aut labore corrupti aut voluptas voluptates non explicabo expedita eum nulla quae. ",
                importance = TodoItem.Companion.Importance.LOW,
                finished = true,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "10",
                text = "Id quia expedita est ipsam nostrum est obcaecati ipsam rem commodi incidunt sit aliquid enim qui voluptas itaque qui nihil nesciunt. ",
                importance = TodoItem.Companion.Importance.LOW,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "11",
                text = "Aut unde minus qui odio voluptate aut voluptas impedit. ",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "12",
                text = "Et laudantium reprehenderit nam corrupti sequi a fugiat dolor qui odit velit ut esse dolor non harum illo.",
                importance = TodoItem.Companion.Importance.LOW,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "13",
                text = "Ut error temporibus qui eius sint eos labore accusantium eum quidem libero. ",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "14",
                text = "Ea veritatis omnis eum numquam sint vel quae alias et aliquam eligendi ea quasi dignissimos ea mollitia quidem aut deserunt quae.",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "15",
                text = "Aut totam debitis non galisum porro est molestiae totam aut quod harum et quae dolorem. In perferendis laboriosam eos impedit explicabo aut dolore velit.",
                importance = TodoItem.Companion.Importance.LOW,
                finished = true,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "16",
                text = "Rem assumenda voluptates et soluta officiis ex sapiente assumenda eum eligendi illum nam illo nulla. ",
                importance = TodoItem.Companion.Importance.URGENT,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "17",
                text = "At quibusdam maiores ea animi rerum hic debitis commodi est explicabo quaerat aut galisum sunt aut molestias nostrum!",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "18",
                text = "Aut libero excepturi et mollitia sint et culpa rerum ut reprehenderit deserunt aut voluptas galisum est explicabo modi aut architecto cupiditate. ",
                importance = TodoItem.Companion.Importance.LOW,
                finished = false,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
            TodoItem(
                id = "19",
                text = "Est maiores consequuntur aut magni architecto et vero eius 33 autem voluptatibus.",
                importance = TodoItem.Companion.Importance.NORMAL,
                finished = true,
                created = LocalDateTime.now(),
                deadline = null,
                edited = null
            ),
        )
    } else {
        throw RuntimeException("VERSION.SDK_INT < O")
    }


    override fun getTodoList(callback: (List<TodoItem>)->Unit): List<TodoItem>{
        callbackListener = callback
        callback.invoke(todoList)
        return todoList
    }


    override fun getTodoItem(id: String): TodoItem {
        return todoList.first { it.id == id }
    }

    override fun addTodoItem(todoItem: TodoItem) {
        val lastId = todoList.last().id
        todoList.add(todoItem.copy(id = lastId+1))
        updateLiveData()
    }

    override fun deleteTodoItem(todoId: String) {
        val item = todoList.first { it.id == todoId }
        todoList.remove(item)
        updateLiveData()
    }

    override fun deleteTodoItem(todoItem: TodoItem) {
        todoList.remove(todoItem)
        updateLiveData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun editTodoItem(todoItem: TodoItem) {
        val oldTodoItem = todoList.first { it.id == todoItem.id }
        if (todoList.contains(oldTodoItem)){
            todoList.remove(oldTodoItem)
        }
        todoList.add(todoItem.copy(edited = LocalDateTime.now()))
        updateLiveData()
    }
    private fun updateLiveData(){
        todoList.sortBy {
            it.id.toInt()
        }
        callbackListener?.invoke(todoList)
    }

}