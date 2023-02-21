package com.pap.majika.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.pap.majika.MajikaApp
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.PaymentStatus
import com.pap.majika.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaymentStatusViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PaymentStatus())
    val uiState: StateFlow<PaymentStatus> = _uiState.asStateFlow()
    private val apiClient = MajikaApi.getInstance()

    // Handle business logic
    fun postTransaction(transactionId: String) {
        viewModelScope.launch {
            appRepository.clearCart()
            _uiState.value = PaymentStatus(
                transactionId,
                apiClient.postPayment(transactionId).status,
            )
        }
    }

    fun reset() {
        _uiState.value = PaymentStatus()
    }

    //    Factory
    companion object {
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as MajikaApp
                val appRepository = application.getAppRepository()
                return PaymentStatusViewModel(appRepository) as T
            }
        }
    }
}