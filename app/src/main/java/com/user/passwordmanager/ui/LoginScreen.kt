package com.user.passwordmanager.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val PIN_LENGTH = 6
@Composable
fun LoginScreen(
    isRegistering:Boolean,
    correctPin: String?=null,
    onAuthSuccess: (String) -> Unit
) {
    var inputPin by remember { mutableStateOf("") }
    var savedPin by remember { mutableStateOf("") }
    var isConfirmStep by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }

    //when理解为Switch Case就好 通过这个format   (某某东西 -> 要执行的内容)
    val title = when {
        isRegistering && !isConfirmStep -> "Set PIN"
        isRegistering && isConfirmStep  -> "Confirm PIN"
        else                            -> "Enter PIN"
    }

    val subtitle = when {
        isError && isRegistering                  -> "PIN do not match"
        isError                                   -> "Incorrect PIN"
        isRegistering && !isConfirmStep           -> "Choose a $PIN_LENGTH-digit PIN"
                isRegistering && isConfirmStep    -> "Re-enter your PIN"
        else                                      -> "4 - 6 digits"
    }

    //LauchedEffect是持续监听一个状态的改变,每次value change就执行一次内容,如果监听Unit(理解为Void就好)就可以做到启动的时候只执行一次
    LaunchedEffect(inputPin) {
        if (inputPin.length < PIN_LENGTH) return@LaunchedEffect

        if (isRegistering) {
                // 注册：满6位自动进入下一步或确认
                if (!isConfirmStep) {
                    savedPin = inputPin
                    inputPin = ""
                    isConfirmStep = true
                } else {
                    if (inputPin == savedPin) {
                        onAuthSuccess(inputPin)
                    } else {
                        isError = true
                        inputPin = ""
                    }
                }
        } else {
            // 登录：每次输入都检查
            if (inputPin == correctPin) {
                onAuthSuccess(inputPin)
            } else {
                isError = true
                inputPin = ""
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isError) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            PinDots(
                length = inputPin.length,
                maxLength = PIN_LENGTH
            )

            PinKeypad(
                onNumberClick = { digit ->
                    if (inputPin.length < PIN_LENGTH) {
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

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PinDots(length: Int, maxLength: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxLength) { index ->
            val filled = index < length
            val color by animateColorAsState(
                targetValue = if (filled) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                label = "dot_color"
            )
            Box(
                modifier = Modifier
                    .size(if (filled) 16.dp else 12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun PinKeypad(
    onNumberClick: (String) -> Unit,
    onBackspace: () -> Unit
) {
    val keys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "back")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keys.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                row.forEach { key ->
                    when (key) {
                        "" -> Spacer(modifier = Modifier.size(80.dp))
                        "back" -> Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(40.dp))
                                    .clickable { onBackspace() },
                                contentAlignment = Alignment.Center
                                ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                                    contentDescription = "Backspace",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp)
                                    )
                                }
                        else -> Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(40.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { onNumberClick(key) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = key,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Light,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
