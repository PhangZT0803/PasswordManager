package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.user.passwordmanager.viewmodel.SettingViewModel
import com.user.passwordmanager.components.PinInputField
@Composable
fun SettingScreen(viewModel: SettingViewModel) {
    val setting by viewModel.setting.collectAsState()
    var showPinDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Theme Settings Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Palette, contentDescription = "Theme")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("App Theme", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Theme 0 = System, 1 = Light, 2 = Dark
                    TextButton(
                        onClick = { viewModel.updateTheme(0) },
                        enabled = setting?.mainTheme != 0
                    ) { Text("System") }

                    TextButton(
                        onClick = { viewModel.updateTheme(1) },
                        enabled = setting?.mainTheme != 1
                    ) { Text("Light") }

                    TextButton(
                        onClick = { viewModel.updateTheme(2) },
                        enabled = setting?.mainTheme != 2
                    ) { Text("Dark") }
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Delete, contentDescription = "Swipe")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Swipe to Delete", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { viewModel.updateSwipeDirection(0) },
                        enabled = setting?.swipeDirection != 0
                    ) { Text("Swipe Left") }
                    TextButton(
                        onClick = { viewModel.updateSwipeDirection(1) },
                        enabled = setting?.swipeDirection != 1
                    ) { Text("Swipe Right") }
                }
            }
        }

        // Security Settings Card
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "Security")
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { showPinDialog = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Change App PIN")
                    }

                }
            }
        }
    }
    if (showPinDialog) {
        ChangePinDialog(
            currentPin = setting?.appPIN ?: "",
            onDismiss = { showPinDialog = false },
            onSaveSuccess = { newPin ->
                viewModel.updatePin(newPin)
                showPinDialog = false
            }
        )
    }
}
            @Composable
            fun ChangePinDialog(
                currentPin: String, // 传入当前数据库里的正确旧密码
                onDismiss: () -> Unit, // 点击取消或背景时的回调
                onSaveSuccess: (String) -> Unit // 保存成功后的回调，把新密码传出去
            ) {
                var oldPin by remember { mutableStateOf("") }
                var newPin by remember { mutableStateOf("") }
                var confirmPin by remember { mutableStateOf("") }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                // 使用最基础的 Dialog 开启一个弹层
                Dialog(
                    onDismissRequest = onDismiss,
                    // 可选：设置点击外部不消失，强制用户点取消
                    properties = DialogProperties(dismissOnClickOutside = false)
                ) {
                    // 自己画弹窗的背景卡片
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp), // 漂亮的圆角
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp), // 卡片内部的边距
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // 标题
                            Text(
                                text = "Change App PIN",
                                style = MaterialTheme.typography.titleLarge
                            )

                            // 1. 旧密码输入框 (复用我们刚才抽离的组件)
                            PinInputField(
                                value = oldPin,
                                onValueChange = { oldPin = it; errorMessage = null },
                                label = "Old PIN",
                                isError = errorMessage == "Incorrect Old PIN",
                                errorMessage = if (errorMessage == "Incorrect Old PIN") errorMessage else null
                            )

                            // 2. 新密码输入框
                            PinInputField(
                                value = newPin,
                                onValueChange = { newPin = it; errorMessage = null },
                                label = "New PIN"
                            )

                            // 3. 确认新密码输入框
                            PinInputField(
                                value = confirmPin,
                                onValueChange = { confirmPin = it; errorMessage = null },
                                label = "Confirm New PIN"
                            )

                            // 统一的错误提示区域
                            if (errorMessage != null && errorMessage != "Incorrect Old PIN") {
                                Text(
                                    text = errorMessage!!,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 底部按钮行
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(onClick = onDismiss) {
                                    Text("Cancel")
                                }
                                Button(
                                    onClick = {
                                        // 校验逻辑
                                        when {
                                            oldPin != currentPin -> errorMessage = "Incorrect Old PIN"
                                            newPin.length < 4 -> errorMessage = "PIN must be 4-6 digits"
                                            newPin != confirmPin -> errorMessage = "New PINs do not match"
                                            else -> {
                                                // 成功！把新密码回调给 SettingScreen
                                                onSaveSuccess(newPin)
                                            }
                                        }
                                    },
                                    modifier = Modifier.padding(start = 8.dp)
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
}