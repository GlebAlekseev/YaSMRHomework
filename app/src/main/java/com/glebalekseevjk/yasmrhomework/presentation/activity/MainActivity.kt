package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.data.remote.RetrofitClient
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import com.glebalekseevjk.yasmrhomework.presentation.fragment.AuthFragment
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoListFragment
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            (this.application as MainApplication).mainViewModelFactory
        )[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (mainViewModel.isAuth) {
            launchFragment(TodoListFragment())
        }else {
            launchFragment(AuthFragment())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        code?.let {
            mainViewModel.updateTokenPair(code) { it ->
                when (it.status) {
                    ResultStatus.SUCCESS -> {
                        if (mainViewModel.isAuth) {
                            launchFragment(TodoListFragment())
                        }
                    }
                    ResultStatus.LOADING -> {
                    }
                    ResultStatus.FAILURE -> {
                    }
                    else -> {}
                }
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .commit()
    }
}