package com.pap.majika.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pap.majika.api.Majika
import com.pap.majika.api.RetrofitClient
import com.pap.majika.models.Menu
import kotlinx.coroutines.launch
import retrofit2.await

class MenuViewModel : ViewModel() {
    private var _totalMenuList: List<Menu> = listOf()

    private val _menuList = MutableLiveData<List<Menu>>()
    val menuList = _menuList as LiveData<List<Menu>>

    fun refreshMenuList() {
        viewModelScope.launch {
            try {
                val menu = RetrofitClient.getInstance().create(Majika::class.java).getMenu()
                _totalMenuList = menu.await().data ?: listOf()
                _menuList.value = _totalMenuList
            } catch (e: Exception) {
//                if menu is empty, it will try again in 2 seconds
                Log.e("MenuPageViewModel", "refreshMenuList: ${e.message}")
                Thread.sleep(2000)
                viewModelScope.launch {
                    refreshMenuList()
                }
            }
        }
    }

    fun filterMenuList(search: String, filter: String) {
        _menuList.value = _totalMenuList.filter {
            it.name.contains(search, true) && it.type.contains(filter, true)
        }
    }
}