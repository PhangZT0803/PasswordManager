package com.user.passwordmanager.ui

sealed class Screen(val route: String) {
    object Login : Screen("Login")
    object Main  : Screen("Vault")
    object AddAccount : Screen("AddAccount")
    object EditAccount : Screen("EditAccount/{accountId}"){
        fun createRoute(accountId: Int) = "EditAccount/$accountId"
    }

}