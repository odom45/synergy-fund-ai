package com.synergyfund.ai.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.viewModel
import com.synergyfund.ai.ui.viewmodel.DashboardUiState
import com.synergyfund.ai.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(onOpenChat: () -> Unit) {
    val vm: DashboardViewModel = viewModel()
    val uiState: DashboardUiState by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.load()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.loading -> {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Loading your summary…")
            }
            uiState.error != null -> {
                Text("Error: ${uiState.error}")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.load() }) {
                    Text("Retry")
                }
            }
            uiState.summary != null -> {
                val s = uiState.summary
                Text(text = "Total Balance: ${"%,.2f".format(s!!.totalBalance)}")
                Spacer(Modifier.height(8.dp))
                Text(text = "Daily PnL: ${"%,.2f".format(s.dailyPnl)}")
                Spacer(Modifier.height(8.dp))
                Text(text = "Return: ${"%.2f".format(s.totalReturnPercent)}%")
                Spacer(Modifier.height(24.dp))
                Button(onClick = onOpenChat) {
                    Text("Open Chat")
                }
            }
            else -> {
                Text("No data available.")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { vm.load() }) {
                    Text("Reload")
                }
            }
        }
    }
}
