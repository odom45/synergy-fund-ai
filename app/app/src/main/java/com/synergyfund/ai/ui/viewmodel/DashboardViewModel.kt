package com.synergyfund.ai.ui.viewmodel
package com.synergyfund.ai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synergyfund.ai.data.ApiClient
import com.synergyfund.ai.data.models.UserSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val summary: UserSummary? = null
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun load() {
        if (_uiState.value.loading || _uiState.value.summary != null) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val summary = ApiClient.api.getUserSummary()
                _uiState.value = DashboardUiState(loading = false, summary = summary)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    loading = false,
                    error = e.message ?: "Failed to load summary"
                )
            }
        }
    }
}
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synergyfund.ai.data.ApiClient
import com.synergyfund.ai.data.models.UserSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val summary: UserSummary? = null
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun load() {
        if (_uiState.value.loading || _uiState.value.summary != null) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                val summary = ApiClient.api.getUserSummary()
                _uiState.value = DashboardUiState(loading = false, summary = summary)
            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    loading = false,
                    error = e.message ?: "Failed to load summary"
                )
            }
        }
    }
}
