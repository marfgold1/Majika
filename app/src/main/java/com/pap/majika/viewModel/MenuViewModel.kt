package com.pap.majika.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pap.majika.MajikaApp
import com.pap.majika.models.Menu
import com.pap.majika.repository.AppRepository
import kotlinx.coroutines.launch

class MenuViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private var _totalMenuList: List<Menu>? = null

    private val _menuList = MutableLiveData<List<Menu>>()
    val menuList = _menuList as LiveData<List<Menu>>

    fun refreshMenuList() {
        viewModelScope.launch {
            _totalMenuList = appRepository.getMenus()
            Log.d("MenuViewModel", "refreshMenuList: ${_totalMenuList?.size}")
            _menuList.value = _totalMenuList ?: listOf()
            if (_totalMenuList != null && _totalMenuList!!.isEmpty()) {
                viewModelScope.launch {
                }
            }
        }
    }

    fun filterMenuList(search: String, filter: String) {
        _menuList.value = _totalMenuList?.filter {
            it.name.contains(search, true) && it.type.contains(filter, true)
        }
    }

//    Factory
    companion object {
        @Suppress("UNCHECKED_CAST")
        val FACTORY = object : ViewModelProvider.Factory {
            override fun <T: ViewModel> create(
                modelClass: Class<T>,
            extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY]) as MajikaApp
                val appRepository = application.getAppRepository()
                return MenuViewModel(appRepository) as T
            }
        }
    }
}