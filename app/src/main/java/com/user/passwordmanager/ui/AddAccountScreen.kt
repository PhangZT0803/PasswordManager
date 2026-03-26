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
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.Domain.PasswordStrength
import com.user.passwordmanager.Domain.Security
import com.user.passwordmanager.viewmodel.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)//(不是必须要的,因为TopAppBar是实验性API所以需要这个import,实验性API就是google开发团队还没有确定他的parameter或者打算以后删除掉,你import这个就是类似签了我明白用这个的可能后果)
@Composable
fun AddAccountScreen(viewModel: AccountViewModel,navController: NavController,accountToEdit: Account? = null){
    val initialPassword = remember(accountToEdit) {
        accountToEdit?.let { Security.passwordDecryption(it.encryptedPassword) } ?: ""
    }
    //val就是const,只可以read.(不可以被Assign新的Value)
    //var就是可以变的,可以read和write(可以被Assign新的Value)
    //remember是value被放进缓存
    //mutableStateOf意思是监听value当value变就重新compose一次画面
    //remember + mutableStateOf就是当新的画面重新渲染过一次,保住上次输入的value还在
    //用mutableStateOf就要用by Example: val Name by remember{mutableStateOf("Phang")}
    //不使用by的写法: val Name = remember{mutableStateOf("Phang")}
    //用By的情况: Text(Name) ->输出就是Phang, 不用By的情况 Text(Name.value) ->输出就是Phang, mutableStateOf会把value assign给一个特殊的叫mutable类型,是Delegation委托关系
    //总结var就用by,val就用=,用by就不需要写 .value直接使用Name.
    //数据类型 Account? 意思是说他是一个Account type然后可能是Null,可以被AssignNull
    // Account和Account?, Int 和Int? 是不一样的类型.带问号的都是表明这个可能是Int也可能是Null,也就是说可以被Assign Int也可以被Assign NULL.
    // (Name ?: "") 意思是说如果左边的 Name是Null那么就默认是右边的.
    var webSiteName by remember{mutableStateOf(accountToEdit?.webSiteName ?: "") }
    var userName by remember{mutableStateOf(accountToEdit?.userName ?: "")}
    var password by remember{mutableStateOf( initialPassword )}
    var webSiteUrl by remember{mutableStateOf(accountToEdit?.webSiteUrl ?:"")}
    val isEditing = accountToEdit != null
    var isUrlAutoSyncEnabled by remember { mutableStateOf(!isEditing) }

    Scaffold(
    topBar = {
        TopAppBar(
            title = { Text(if (isEditing) "Edit Account" else "Add Account")},
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
                onValueChange = { newWebSiteName:String ->
                    webSiteName = newWebSiteName
                    if (isUrlAutoSyncEnabled) {
                        webSiteUrl = newWebSiteName.lowercase().replace(" ", "")+".com"
                    }
                },
                label = { Text("Website Name")},
                placeholder = { Text("e.g. Facebook") },
                modifier = Modifier.fillMaxWidth()
             )
            OutlinedTextField(
                value = webSiteUrl,
                onValueChange = {  newWebsiteUrl:String ->
                    webSiteUrl = newWebsiteUrl
                    isUrlAutoSyncEnabled = false
                },
                label = { Text("Website URL") },
                placeholder = { Text("e.g. facebook.com") },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                        value = userName,
                        onValueChange = { newUserName:String -> userName = newUserName },
                        label = { Text("User Name") },
                        modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                        value = password,
                        onValueChange = { newPassword:String -> password = newPassword },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (webSiteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank()&& webSiteUrl.isNotBlank()) {
                        if (isEditing) {
                            viewModel.updateAccount(accountToEdit.accountId, webSiteName, userName, password, webSiteUrl)
                        } else {
                            viewModel.addAccount(webSiteName, userName, password, webSiteUrl)
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = webSiteName.isNotBlank() && userName.isNotBlank() && password.isNotBlank() && webSiteUrl.isNotBlank()
            ) {
                Text(if (isEditing) "Save Changes" else "Add Account")
            }
        }
    }
}