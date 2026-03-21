package com.user.passwordmanager

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.user.passwordmanager.ui.AddAccountScreen
import com.user.passwordmanager.ui.LoginScreen
import com.user.passwordmanager.ui.PasswordManagerScreen
import com.user.passwordmanager.ui.Screen
import com.user.passwordmanager.ui.theme.PasswordManagerTheme
import com.user.passwordmanager.viewmodel.AccountViewModel
import com.user.passwordmanager.viewmodel.AuthState
import com.user.passwordmanager.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PasswordManagerTheme {
                val navController = rememberNavController()
                val accountViewModel: AccountViewModel = hiltViewModel()
                val authViewModel: AuthViewModel = hiltViewModel()
                val authState by authViewModel.authState.collectAsState()

                when (val state = authState) {
                    is AuthState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    else -> {
                        NavHost(
                            navController = navController,
                            startDestination = if (state is AuthState.Authenticated) Screen.Main.route else Screen.Login.route
                        ) {
                            composable(Screen.Login.route) {
                                val isRegistering = state is AuthState.Registering
                                val correctPin =
                                    if (state is AuthState.RequireLogin) state.correctPin else null

                                LoginScreen(
                                    isRegistering = isRegistering,
                                    correctPin = correctPin,
                                    onAuthSuccess = { newPin ->
                                        if (isRegistering) {
                                            authViewModel.saveAppPin(newPin)
                                        } else {
                                            authViewModel.setAuthenticated()
                                        }
                                        navController.navigate(Screen.Main.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable(Screen.Main.route) {
                                PasswordManagerScreen(
                                    viewModel = accountViewModel,
                                    navController = navController
                                )
                            }
                            composable(Screen.AddAccount.route) {
                                AddAccountScreen(
                                    viewModel = accountViewModel,
                                    navController = navController,
                                    accountToEdit = null
                                )
                            }
                            composable(Screen.EditAccount.route) { backStackEntry ->
                                val accountIdStr = backStackEntry.arguments?.getString("accountId")
                                val accountId = accountIdStr?.toIntOrNull()
                                val accounts by accountViewModel.accounts.collectAsState()
                                val accountToEdit = accounts.find { it.accountId == accountId }

                                if (accountToEdit != null) {
                                    AddAccountScreen(
                                        viewModel = accountViewModel,
                                        navController = navController,
                                        accountToEdit = accountToEdit
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

