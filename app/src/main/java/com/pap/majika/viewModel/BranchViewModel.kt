package com.pap.majika.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.Branch
import com.pap.majika.models.Response
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BranchViewModel : ViewModel() {
    // If null, then is getting GET data, if 0 size then is empty.
    private val _uiState = MutableStateFlow<Response<List<Branch>>?>(null)
    val uiState: StateFlow<Response<List<Branch>>?> = _uiState.asStateFlow()
    private val apiClient = MajikaApi.getInstance()

    init {
        getBranches()
    }

    fun getBranches() {
        _uiState.value = null
        viewModelScope.launch {
            try {
                _uiState.value = apiClient.getBranch()
            } catch (e: Exception) {
                _uiState.value = Response() // empty response on error
                Log.e("BranchView", "Error when retrieving branch data.", e)
            }
        }
    }
}