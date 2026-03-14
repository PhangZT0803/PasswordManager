package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.ui.theme.passwordTypography
import com.user.passwordmanager.ui.theme.usernameTypography
import com.user.passwordmanager.ui.theme.websiteTypography

@Composable
 fun searchBar(query: String, onQueryChange: (String)-> Unit){
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = {Text("WebsiteName")},
        leadingIcon ={ Icon(Icons.Default.Search, contentDescription = null)},
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun AccountItem(account: Account){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)){
            Text(text = account.webSiteName, style = websiteTypography.titleMedium)
            Text(text = account.userName, style = usernameTypography.bodyMedium)
            Text(text = account.encryptedPassword, style = passwordTypography.bodyMedium)
        }
    }
}

@Composable
fun AppNavigationBar(currentRoute: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar { // 使用标准的 Material3 导航栏
        NavigationBarItem(
            selected = currentRoute == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Lock, contentDescription = "Vault") },
            label = { Text("Vault") }
        )
        NavigationBarItem(
            selected = currentRoute == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.Refresh, contentDescription = "Generator") },
            label = { Text("Generator") }
        )
        NavigationBarItem(
            selected = currentRoute == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Setting") },
            label = { Text("Setting") }
        )
    }
}

@Composable
private fun tabItem(title:String){
    Row(modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(title)
    }
}