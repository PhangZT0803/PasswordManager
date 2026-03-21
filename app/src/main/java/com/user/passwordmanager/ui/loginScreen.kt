package com.user.passwordmanager.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.newStringBuilder

@Composable
fun LoginScreen(
    isRegistering:Boolean,
    correctPin: String?=null,
    onAuthSuccess: (String) -> Unit
) {
    var inputPin by remember { mutableStateOf("") }
    var confirmPin by remember {mutableStateOf("")}
    var isError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if(isRegistering) "Set App PIN" else "Authentication",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Enter 4-6 PIN Number",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputPin,
            onValueChange = { newInputPin:String -> if (newInputPin.length <= 6) { inputPin=newInputPin; isError = false }},
            label = { Text(if (isRegistering) "Set PIN" else "PIN Number") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (isRegistering) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPin,
                onValueChange = { newConfirmPin:String -> if(newConfirmPin.length <= 6) {confirmPin = newConfirmPin; isError = false } },
                label = { Text("Confirm PIN") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                isError = isError,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (isError) {
            Text(
                text = if (isRegistering) "Pin do not Match!" else "Incorrect PIN",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isRegistering) {
                    if (inputPin == confirmPin) {
                        onAuthSuccess(inputPin)
                    } else {
                        isError = true
                        inputPin = ""
                        confirmPin = ""
                    }
                }
                else if (inputPin == correctPin) {
                    onAuthSuccess(inputPin)
                } else {
                    isError = true
                    inputPin = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = inputPin.isNotEmpty()
        ) {
            Text("Unlock", fontSize = 18.sp)
        }
    }
}