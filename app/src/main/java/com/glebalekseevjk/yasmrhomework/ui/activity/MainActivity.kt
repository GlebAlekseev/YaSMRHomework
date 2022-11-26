package com.glebalekseevjk.yasmrhomework.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.ui.application.MainApplication
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.MainViewModel
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            (this.application as MainApplication).mainViewModelFactory
        )[MainViewModel::class.java]
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mainNv: NavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initNavigationUI()
        initListeners()
        checkAuth()
    }

    private fun checkAuth() {
        if (mainViewModel.isAuth) {
            navController.navigate(R.id.action_authFragment_to_todoListFragment)
        }
    }

    private fun initViews() {
        drawerLayout = findViewById(R.id.drawer_layout)
        mainNv = findViewById(R.id.main_nv)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nhf) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initNavigationUI() {
        NavigationUI.setupWithNavController(mainNv, navController)
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.todoFragment -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
        val headerView = mainNv.getHeaderView(0)
        val tokenStorage = (this.application as MainApplication).sharedPreferencesTokenStorage
        val loginTv: TextView = headerView.findViewById(R.id.login_tv)
        loginTv.text = "@${tokenStorage.getLogin()}"
        val nameTv: TextView = headerView.findViewById(R.id.name_tv)
        nameTv.text = tokenStorage.getDisplayName()
        mainNv.setNavigationItemSelectedListener {
            onDrawerItemSelected(it)
        }
    }

    private fun onDrawerItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.exit_menu_item -> {
                mainViewModel.logout {
                    if (navController.currentDestination?.id == R.id.todoListFragment) {
                        navController.navigate(R.id.action_todoListFragment_to_authFragment)
                    }
                }
                return true
            }
            else -> {}
        }
        return false
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val code = intent?.data?.getQueryParameter("code")
        code?.let {
            mainViewModel.updateTokenPair(code) { result ->
                when (result.status) {
                    ResultStatus.SUCCESS -> {
                        if (mainViewModel.isAuth) {
                            navController.navigate(R.id.action_authFragment_to_todoListFragment)
                        }
                    }
                    ResultStatus.LOADING -> {
                    }
                    ResultStatus.FAILURE -> {
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}