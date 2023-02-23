package com.pap.majika.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.pap.majika.MajikaApp
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.repository.AppRepository
import kotlinx.coroutines.launch

class MenuViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    //    Menu
    private val _menuList = MutableLiveData<Map<Menu, CartItem>>()
    val menuList : LiveData<Map<Menu, CartItem>> = _menuList
    //    Cart
    private var _cartList = MutableLiveData<Map<Menu, CartItem>>()
    val cartList : LiveData<Map<Menu, CartItem>> = _cartList

    private var currentFilter: Pair<String, String> = Pair("", "")

    private val observer : Observer<Map<Menu, CartItem>>



    init {
        refreshMenuList()
        observer = Observer { entry ->
            _menuList.value = entry.filter {
                it.key.name.contains(currentFilter.first, true)
                        && it.key.type.contains(currentFilter.second)
            }
            _cartList.value = entry.filter { it.value.quantity > 0 }
        }
        appRepository.menusWithCartItem.observeForever(observer)
    }

    fun refreshMenuList() {
        viewModelScope.launch {
            appRepository.refreshMenusWithCartItem()
        }
    }

    fun filterMenuList(search: String, filter: String) {
        Log.d("MenuViewModel", "filterMenuList: $search, $filter")
        if (currentFilter == Pair(search, filter)) return
        currentFilter = Pair(search, filter)
        viewModelScope.launch {
            _menuList.value = appRepository.getMenusWithCartItem().filter {
                it.key.name.contains(search, true)
                        && it.key.type.contains(filter)
            }
        }
    }

    fun addToCart(menu: Menu, qty: Int) {
        viewModelScope.launch {
            appRepository.addCartItem(menu, qty)
        }
    }

    fun removeFromCart(menu: Menu, qty: Int) {
        viewModelScope.launch {
            appRepository.removeCartItem(menu, qty)
        }
    }

    override fun onCleared() {
        appRepository.menusWithCartItem.removeObserver(observer)
        super.onCleared()
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