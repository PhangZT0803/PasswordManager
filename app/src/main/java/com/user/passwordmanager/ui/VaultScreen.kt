package com.user.passwordmanager.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.user.passwordmanager.R
import com.user.passwordmanager.data.Account
import com.user.passwordmanager.Domain.Security
import com.user.passwordmanager.ui.theme.passwordTypography
import com.user.passwordmanager.ui.theme.usernameTypography
import com.user.passwordmanager.ui.theme.websiteTypography
import com.user.passwordmanager.viewmodel.AccountViewModel

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
        shape = RoundedCornerShape(14.dp),
        singleLine = true
    )
}
@Composable
fun getStrengthAppearance(score: Int): Pair<String, Color> {
    return when (score) {
        0 -> "Very Weak" to Color(0xFFD32F2F)  // 红色
        1 -> "Weak" to Color(0xFFF57C00)       // 橙色
        2 -> "Medium" to Color(0xFFFBC02D)      // 黄色
        3 -> "Strong" to Color(0xFF388E3C)      // 绿色
        4 -> "Very Strong" to Color(0xFF1B5E20) // 深绿
        else -> "Unknown" to Color.Gray
    }
}
@Composable
fun AccountItem(account: Account,onEditClick:()->Unit,onCopyClick: () -> Unit) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val decryptedPassword = remember(account.encryptedPassword) {
        Security.passwordDecryption(account.encryptedPassword)
    }
    val (strengthText, strengthColor) = getStrengthAppearance(account.passwordStrength)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {isPasswordVisible = !isPasswordVisible },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .heightIn(min= 72.dp)
                    .fillMaxWidth()
                    .background(
                        color = strengthColor,
                        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 12.dp)
                    .weight(1f)
            ) {
                //Logo
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    AsyncImage(
                        model = "https://www.google.com/s2/favicons?domain=${account.webSiteUrl}&sz=128",
                        contentDescription = "Website Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        error = painterResource(R.drawable.ic_launcher_foreground),
                        placeholder = painterResource(R.drawable.ic_launcher_foreground)
                    )
                }//End of Logo

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.webSiteName,
                        style = websiteTypography.bodyLarge
                    )
                    Text(
                        text = account.userName,
                        style = usernameTypography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (isPasswordVisible) decryptedPassword else "••••••",
                        style = passwordTypography.bodyLarge
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Box(
                        modifier = Modifier
                            .background(
                                color = strengthColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 7.dp, vertical = 3.dp)
                    ){
                        Text(
                            text = strengthText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = strengthColor
                        )
                    }
                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onCopyClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    }
                }
            }
        }
    }
}

@Composable
fun SwipeDeleteBackground(swipeDirection: Int,isActive: Boolean){
    val alignment = if (swipeDirection == 0) Alignment.CenterEnd else Alignment.CenterStart
    val iconPadding = if (swipeDirection == 0)
        Modifier.padding(end = 24.dp)
    else
        Modifier.padding(start = 24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(
                color = if(isActive)Color(0xFFD32F2F) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = alignment
    ){
        if (isActive){
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = iconPadding
            )
        }
    }
}
    @Composable
    fun AppNavigationBar(currentRoute: Int, onTabSelected: (Int) -> Unit) {
        NavigationBar {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaultScreen(accounts: List<Account>, searchText: String, viewModel: AccountViewModel, navController: NavController,swipeDirection: Int = 0) {
    val context =
        LocalContext.current //类似通行证(studentID),只有使用和系统级别相关的东西才会需要,因为系统需要知道是谁需要这个功能,后面做NotificationListenerService也会有
    var accountToDelete by remember { mutableStateOf<Account?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val filteredList = accounts.filter {
            it.webSiteName.contains(searchText, ignoreCase = true)
        }

        items(filteredList, key = { it.accountId }) { account ->

            val dismissState =
                rememberSwipeToDismissBoxState(initialValue = SwipeToDismissBoxValue.Settled)
            val triggerValue = if (swipeDirection==0){
                SwipeToDismissBoxValue.EndToStart
            }else{
                SwipeToDismissBoxValue.StartToEnd
            }
            LaunchedEffect(dismissState.currentValue) {//监听 dismissState的currentValue,
                if (dismissState.currentValue == triggerValue) {
                    accountToDelete = account
                    showDeleteDialog = true
                    dismissState.snapTo(SwipeToDismissBoxValue.Settled) // 弹回
                }
            }

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromEndToStart = swipeDirection == 0, //left
                enableDismissFromStartToEnd = swipeDirection == 1, //right
                backgroundContent = {
                    SwipeDeleteBackground(
                        swipeDirection = swipeDirection,
                        isActive = dismissState.targetValue == triggerValue
                    )
                }
            ) {
                AccountItem(
                    account = account,
                    onEditClick = {
                        navController.navigate(Screen.EditAccount.createRoute(account.accountId))
                    },
                    onCopyClick = {
                        val decrypted = Security.passwordDecryption(account.encryptedPassword)
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("password", decrypted))
                    }
                )
            }

            if (showDeleteDialog && accountToDelete != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        accountToDelete = null
                    },
                    title = { Text("Confirm Deletion") },
                    text = { Text("Are you sure you want to delete '${accountToDelete?.webSiteName}','${accountToDelete?.userName}'?") },
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
    }
}