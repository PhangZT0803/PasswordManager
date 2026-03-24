package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.user.passwordmanager.viewmodel.AccountViewModel
import com.user.passwordmanager.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerScreen(viewModel: AccountViewModel,navController: NavController,settingViewModel: SettingViewModel) {

    var searchText by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val accounts by viewModel.accounts.collectAsState()

    Scaffold(
        topBar = {
            if (selectedTabIndex == 0) {
                SearchBar(
                    query = searchText,
                    onQueryChange = { searchText = it }
                )
            } else {
                TopAppBar(title = { Text(if (selectedTabIndex == 1) "Password Generator" else "Setting") })
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddAccount.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Account")
            }
        },
        bottomBar = {
            AppNavigationBar(
                currentRoute = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (selectedTabIndex) {
                0 -> {
                    VaultScreen(accounts, searchText, viewModel, navController,settingViewModel.setting.collectAsState().value?.swipeDirection ?: 0)
                }
                //1 -> { GeneratorScreen() }
                2 -> {
                    SettingScreen(settingViewModel)
                }
            }
        }
    }
}

