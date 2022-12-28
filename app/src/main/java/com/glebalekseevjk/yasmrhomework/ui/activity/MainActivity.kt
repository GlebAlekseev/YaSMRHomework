package com.glebalekseevjk.yasmrhomework.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TokenStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.glebalekseevjk.yasmrhomework.R
import com.glebalekseevjk.yasmrhomework.di.FromViewModelFactory
import com.glebalekseevjk.yasmrhomework.domain.entity.ResultStatus
import com.glebalekseevjk.yasmrhomework.ui.viewmodel.MainViewModel
import com.glebalekseevjk.yasmrhomework.utils.appComponent
import com.google.android.material.navigation.NavigationView
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainViewModel: MainViewModel


    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.createMainActivitySubcomponent().inject(this)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        initNavigationUI()
        initListeners()
        if (savedInstanceState == null){
            checkAuth()
        }
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

    private fun checkAuth() {
        if (mainViewModel.isAuth) {
            navController.navigate(R.id.action_authFragment_to_todoListFragment)
        }
    }

    private fun initNavigationUI() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nhf) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.mainNv, navController)
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                R.id.todoFragment -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }

        // TODO: Сделать isAuth flow, реагировать в MainActivity.

        val navDrawerHeaderBinding = DataBindingUtil.inflate<NavDrawerHeaderBinding>(layoutInflater,R.layout.nav_drawer_header, binding.mainNv,false)
        navDrawerHeaderBinding.mainViewModel = mainViewModel
        binding.mainNv.addHeaderView(navDrawerHeaderBinding.root)
        binding.navigationItemSelectedListener = NavigationView.OnNavigationItemSelectedListener {
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
}