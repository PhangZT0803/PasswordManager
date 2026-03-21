package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.viewmodel.AccountViewModel
@Composable
fun PasswordManagerScreen(viewModel: AccountViewModel,navController: NavController) {
    val accounts by viewModel.accounts.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var accountToDelete by remember { mutableStateOf<Account?>(null) }

    Scaffold(
        topBar = {
            SearchBar(
                query = searchText,
                onQueryChange = { searchText = it }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddAccount.route) }) {
                Icon(Icons.Default.Add, contentDescription ="Add Account")
            }
        },
        bottomBar = {
            AppNavigationBar(
                currentRoute = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val filteredList = accounts.filter {
                it.webSiteName.contains(searchText, ignoreCase = true)
            }
            items(filteredList, key = { it.accountId }){ account ->
                AccountItem(account = account, onEditClick = {
                    navController.navigate(Screen.EditAccount.createRoute(account.accountId))
                },
                    onDeleteClick = {
                        accountToDelete = account
                        showDeleteDialog = true
                    })
            }
        }
    }
    if (showDeleteDialog && accountToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete '${accountToDelete?.webSiteName}'?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAccount(accountToDelete!!)
                    showDeleteDialog = false
                    accountToDelete = null
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    accountToDelete = null
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

