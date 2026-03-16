package com.user.passwordmanager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        val repository = AccountRepository(database.AccountDao(), database.SettingDao())
        val viewModelFactory = AccountViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                var storedPin by remember { mutableStateOf<String?>(null) }
                var isRegistering by remember { mutableStateOf(true) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    val setting = database.SettingDao().getSetting()
                    if (setting != null && setting.SetPIN) {
                        storedPin = setting.appPIN
                        isRegistering = false
                    } else {
                        isRegistering = true
                    }
                    isLoading = false
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = "Login"
                    ) {
                        composable("Login") {
                            LoginScreen(
                                isRegistering = isRegistering,
                                correctPin = storedPin,
                                onAuthSuccess = { newPin ->
                                    if (isRegistering) {
                                        viewModel.saveAppPin(newPin)
                                        storedPin = newPin
                                        isRegistering = false
                                    }
                                })
                            navController.navigate("main") {
                                popUpTo("Login") {
                                    inclusive = true
                                }
                            }
                        }
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
}

