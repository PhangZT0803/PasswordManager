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
import androidx.compose.runtime.LaunchedEffect
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
            currentPin = setting?.appPIN?:"",
            onDismiss = { showPinDialog = false },
            onSaveSuccess = { newPin ->
                viewModel.updatePin(newPin)
                showPinDialog = false
            }
        )
    }
}
private enum class ChangePinStep { VERIFY_OLD, ENTER_NEW, CONFIRM_NEW }
@Composable
fun ChangePinDialog(
    currentPin: String,
    onDismiss: () -> Unit, // 点击取消或背景时的回调
    onSaveSuccess: (String) -> Unit // 保存成功后的回调，把新密码传出去
) {
    var step by remember { mutableStateOf(ChangePinStep.VERIFY_OLD) }
    var inputPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    val title = when (step) {
        ChangePinStep.VERIFY_OLD  -> "Enter Old PIN"
        ChangePinStep.ENTER_NEW   -> "Enter New PIN"
        ChangePinStep.CONFIRM_NEW -> "Confirm New PIN"
    }

    val subtitle = when {
        isError && step == ChangePinStep.VERIFY_OLD  -> "Incorrect PIN ,$currentPin,$isError,$step"
        isError && step == ChangePinStep.CONFIRM_NEW -> "PINs do not match"
        else -> "6-digit PIN"
    }

    LaunchedEffect(inputPin) {
        if (inputPin.length < 6) return@LaunchedEffect

        when (step) {
            ChangePinStep.VERIFY_OLD -> {
                if (inputPin == currentPin) {
                    step = ChangePinStep.ENTER_NEW
                    inputPin = ""
                    isError = false
                } else {
                    isError = true
                    inputPin = ""
                }
            }
            ChangePinStep.ENTER_NEW -> {
                newPin = inputPin
                inputPin = ""
                step = ChangePinStep.CONFIRM_NEW
            }
            ChangePinStep.CONFIRM_NEW -> {
                if (inputPin == newPin) {
                    onSaveSuccess(inputPin)
                } else {
                    isError = true
                    inputPin = ""
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 标题
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // PIN 点
                PinDots(length = inputPin.length, maxLength = 6)

                // 数字键盘
                PinKeypad(
                    onNumberClick = { digit ->
                        if (inputPin.length < 6) {
                            inputPin += digit
                            isError = false
                        }
                    },
                    onBackspace = {
                        if (inputPin.isNotEmpty()) {
                            inputPin = inputPin.dropLast(1)
                            isError = false
                        }
                    }
                )

                // 取消按钮
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}