package com.synergyfund.ai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synergyfund.ai.data.ApiClient
import com.synergyfund.ai.data.SessionManager
import com.synergyfund.ai.data.models.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(loading = true, error = null)
            try {
                val response = ApiClient.api.login(LoginRequest(email, password))
                SessionManager.setToken(response.token)
                _uiState.value = LoginUiState(loading = false, error = null)
                onSuccess()
            } catch (e: Exception) {
                _uiState.value = LoginUiState(
                    loading = false,
                    error = e.message ?: "Login failed"
                )
            }
        }
    }
}
