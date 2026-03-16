package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.user.passwordmanager.R
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.ui.theme.passwordTypography
import com.user.passwordmanager.ui.theme.usernameTypography
import com.user.passwordmanager.ui.theme.websiteTypography

@Composable
 fun SearchBar(query: String, onQueryChange: (String)-> Unit){
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
fun AccountItem(account: Account) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = "https://www.google.com/s2/favicons?domain=${account.webSiteUrl}&sz=128",
                contentDescription = "Website Logo",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(8.dp)),
                error = painterResource(R.drawable.ic_launcher_foreground),
                placeholder = painterResource(R.drawable.ic_launcher_foreground)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = account.webSiteName, style = websiteTypography.titleMedium)
                Text(text = account.userName, style = usernameTypography.bodyMedium)
                Text(text = account.encryptedPassword, style = passwordTypography.bodyMedium)
            }
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