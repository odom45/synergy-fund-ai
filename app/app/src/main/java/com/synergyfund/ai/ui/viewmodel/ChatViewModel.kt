package com.synergyfund.ai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synergyfund.ai.data.ApiClient
import com.synergyfund.ai.data.models.ChatRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ChatMessage(val fromUser: Boolean, val text: String)

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(ChatMessage(false, "Hi! I’m the SynergyFund AI assistant.")),
    val sending: Boolean = false,
    val error: String? = null
)

class ChatViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun sendMessage(text: String) {
        if (text.isBlank() || _uiState.value.sending) return
        viewModelScope.launch {
            val current = _uiState.value
            _uiState.value = current.copy(
                messages = current.messages + ChatMessage(true, text),
                sending = true,
                error = null
            )
            try {
                val response = ApiClient.api.chat(ChatRequest(message = text))
                _uiState.value = _uiState.value.copy(
                    messages = _uiState.value.messages + ChatMessage(false, response.reply),
                    sending = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    sending = false,
                    error = e.message ?: "Failed to send message"
                )
            }
        }
    }
}
