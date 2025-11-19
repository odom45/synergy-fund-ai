package com.synergyfund.ai.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.viewModel
import com.synergyfund.ai.ui.viewmodel.ChatMessage
import com.synergyfund.ai.ui.viewmodel.ChatUiState
import com.synergyfund.ai.ui.viewmodel.ChatViewModel

@Composable
fun ChatScreen(onBack: () -> Unit) {
    val vm: ChatViewModel = viewModel()
    val uiState: ChatUiState by vm.uiState.collectAsState()

    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("< Back")
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(uiState.messages) { msg ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = if (msg.fromUser) Arrangement.End else Arrangement.Start
                ) {
                    Text(text = msg.text)
                }
            }
        }

        if (uiState.error != null) {
            Text(text = "Error: ${uiState.error}")
            Spacer(Modifier.height(8.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                label = { Text("Ask about your account or strategies…") },
                enabled = !uiState.sending
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        vm.sendMessage(input)
                        input = ""
                    }
                },
                enabled = !uiState.sending
            ) {
                if (uiState.sending) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(6.dp))
                        Text("Sending…")
                    }
                } else {
                    Text("Send")
                }
            }
        }
    }
}
