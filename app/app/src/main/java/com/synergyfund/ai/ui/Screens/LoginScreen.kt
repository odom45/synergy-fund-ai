package com.synergyfund.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.viewModel
import com.synergyfund.ai.ui.viewmodel.LoginUiState
import com.synergyfund.ai.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val vm: LoginViewModel = viewModel()
    val uiState: LoginUiState by vm.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "SynergyFund AI")
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            )
            Spacer(Modifier.height(8.dp))
            if (uiState.error != null) {
                Text(text = uiState.error!!)
                Spacer(Modifier.height(8.dp))
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    vm.login(email, password) { onLoginSuccess() }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            ) {
                if (uiState.loading) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Signing in…")
                    }
                } else {
                    Text("Sign In")
                }
            }
        }
    }
}
