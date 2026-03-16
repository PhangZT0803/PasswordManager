package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.user.passwordmanager.R
import com.user.passwordmanager.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(viewModel: AccountViewModel,navController: NavController){
    var webSiteName by remember{mutableStateOf("")}
    var userName by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
    var webSiteUrl by remember{mutableStateOf("")}
    var isUrlAutoSyncEnabled by remember { mutableStateOf(true) }

    Scaffold(
    topBar = {
        TopAppBar(
            title = { Text("Add Account")},
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model ="https://www.google.com/s2/favicons?domain=${webSiteUrl}&sz=128",
                contentDescription = "Website Logo",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(R.drawable.ic_launcher_foreground),
            )
            OutlinedTextField(
                value = webSiteName,
                onValueChange = { newValue->
                    webSiteName = newValue
                    if (isUrlAutoSyncEnabled) {
                        webSiteUrl = newValue.lowercase().replace(" ", "")+".com"
                    }
                },
                label = { Text("Website Name")},
                placeholder = { Text("e.g. Facebook") },
                modifier = Modifier.fillMaxWidth()
             )
            OutlinedTextField(
                value = webSiteUrl,
                onValueChange = {  newValue->
                    webSiteUrl = newValue
                    isUrlAutoSyncEnabled = false
                },
                label = { Text("Website URL") },
                placeholder = { Text("e.g. facebook.com") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("User Name") },
                        modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(), // 隐藏密码字符
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (webSiteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank()&& webSiteUrl.isNotBlank()) {
                        viewModel.addAccount(webSiteName, userName, password, webSiteUrl)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = webSiteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank() && webSiteUrl.isNotBlank()
            ) {
                Text("Add Account")
            }
        }
    }
}