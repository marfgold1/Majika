package com.pap.majika.pages.menu

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

class MenuPageViewModel : ViewModel() {
    private var _totalMenuList: List<Menu> = listOf()

    private val _menulist = MutableLiveData<List<Menu>>()
    val menulist = _menulist

    fun refreshMenuList() {
        viewModelScope.launch {
            try {
                val menu = RetrofitClient.getInstance().create(Majika::class.java).getMenu()
                _totalMenuList = menu.await().data ?: listOf()
                _menulist.value = _totalMenuList
            } catch (e: Exception) {
//                if menu is empty, it will try again in 2 seconds
                Log.e("MenuPageViewModel", "refreshMenuList: ${e.message}")
                Thread.sleep(2000)
                refreshMenuList()
            }
        }
    }

    fun filterMenuList(search: String, filter: String) {
        _menulist.value = _totalMenuList.filter {
            it.name.contains(search, true) && it.type.contains(filter, true)
        }
    }
}