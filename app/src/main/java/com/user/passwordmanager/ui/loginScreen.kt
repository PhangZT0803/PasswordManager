package com.user.passwordmanager.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.user.passwordmanager.security.Security
import com.user.passwordmanager.ui.theme.PasswordManagerTheme

@Composable
fun LoginScreen(
    onAuthenticateWithBiometric: () -> Unit,  // 调用 security.showPrompt
    onAuthSuccess: () -> Unit,                // 验证成功时调用
    onAutoTriggerBiometric: () -> Unit        // 自动触发（LaunchedEffect 中用）
) {
    val context = LocalContext.current
    val security = remember { Security() }
    val canUseBiometric = remember { security.canUseBiometric(context) }

    // 自动触发生物识别（只在进入时尝试一次）
    LaunchedEffect(Unit) {
        onAutoTriggerBiometric()
    }

    // 控制 PIN 对话框
    var showPinDialog by remember { mutableStateOf(false) }

    // 无限动画：呼吸效果
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 指纹/人脸动画图标
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier.matchParentSize()
                ) {}
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Vault Encrypted",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Biometric verification required",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 生物识别按钮（如果设备支持则启用）
            Button(
                onClick = onAuthenticateWithBiometric,
                enabled = canUseBiometric,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = if (canUseBiometric) "Authenticate" else "Biometric not available",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 备用选项：使用密码
            TextButton(
                onClick = { showPinDialog = true }
            ) {
                Text("Use PIN instead")
            }
        }

        // PIN 输入对话框
        if (showPinDialog) {
            PinDialog(
                onDismiss = { showPinDialog = false },
                onConfirm = { pin ->
                    // 这里简单验证 PIN 是否为 "1234"，你可改为从 Setting 读取
                    if (pin == "1234") {
                        onAuthSuccess()
                    } else {
                        // 错误提示可以加个 Snackbar，但先简单处理
                    }
                    showPinDialog = false
                }
            )
        }
    }
}

@Composable
fun PinDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Enter PIN",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it.take(6) },
                    label = { Text("PIN (预设 1234)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onConfirm(pin) },
                        enabled = pin.isNotBlank()
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun previewLoginScreen(){
    PasswordManagerTheme() {
        LoginScreen(
            onAuthenticateWithBiometric = {},
            onAuthSuccess = {},
            onAutoTriggerBiometric = {}
        )
    }
}