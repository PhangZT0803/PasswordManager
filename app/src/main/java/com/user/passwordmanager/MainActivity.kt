package com.user.passwordmanager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.user.passwordmanager.data.AccountDatabase
import com.user.passwordmanager.data.AccountRepository
import com.user.passwordmanager.security.Security
import com.user.passwordmanager.ui.AddAccountScreen
import com.user.passwordmanager.ui.LoginScreen
import com.user.passwordmanager.ui.PasswordManagerScreen
import com.user.passwordmanager.ui.theme.PasswordManagerTheme
import com.user.passwordmanager.viewmodel.AccountViewModel
import com.user.passwordmanager.viewmodel.AccountViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Room.databaseBuilder(
            applicationContext,
            AccountDatabase::class.java,
            "account_db"
        )
            .build()

        val repository = AccountRepository(database.AccountDao())
        val viewModelFactory = AccountViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]

        val settingDao = database.SettingDao()


        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main"
                ) {
                    composable("main") {
                        PasswordManagerScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable("AddAccountScreen") {
                        AddAccountScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                }
            }
        }
    }
}

