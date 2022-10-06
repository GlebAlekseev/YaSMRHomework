package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoListFragment


class MainActivity : AppCompatActivity() {
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        // Обработка кода авторизации.
        println("^^^^^^^^^^^^^ $code")
        // Валидация и получение токена..
        launchFragment(TodoListFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .addToBackStack(null)
            .commit()
    }
}