package com.pap.majika.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.PaymentStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentStatusViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentStatus())
    val uiState: StateFlow<PaymentStatus> = _uiState.asStateFlow()
    private val apiClient = MajikaApi.getInstance()

    // Handle business logic
    fun postTransaction(transactionId: String) {
        viewModelScope.launch {
            _uiState.value = PaymentStatus(
                transactionId,
                apiClient.postPayment(transactionId).status,
            )
        }
    }

    fun reset() {
        _uiState.value = PaymentStatus()
    }
}