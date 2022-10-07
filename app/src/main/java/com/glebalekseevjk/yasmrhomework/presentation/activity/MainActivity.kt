package com.glebalekseevjk.yasmrhomework.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesRevisionStorage
import com.glebalekseevjk.yasmrhomework.cache.SharedPreferencesTokenStorage
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.domain.entity.TokenPair
import com.glebalekseevjk.yasmrhomework.presentation.application.MainApplication
import com.glebalekseevjk.yasmrhomework.presentation.fragment.TodoListFragment
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.MainViewModel
import com.glebalekseevjk.yasmrhomework.presentation.viewmodel.TodoViewModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(this,(this.application as MainApplication).mainViewModelFactory)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.checkAuth{
            launchFragment(TodoListFragment())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        code?.let {
            mainViewModel.getAndSetTokens(code){ it, tokenPrefs ->
                when(it.status){
                    ResultStatus.SUCCESS -> {
                        // Сохранить токены
                        tokenPrefs.setTokenPair(it.data)
                        mainViewModel.checkAuth{
                            launchFragment(TodoListFragment())
                        }
                    }
                    ResultStatus.LOADING -> {}
                    ResultStatus.FAILURE -> {}
                    else -> {}
                }
            }
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fcv, fragment)
            .addToBackStack(null)
            .commit()
    }
}