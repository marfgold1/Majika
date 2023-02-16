package com.pap.majika.viewModel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.pap.majika.MajikaApp
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.repository.AppRepository
import kotlinx.coroutines.launch

class MenuViewModel(
    private val appRepository: AppRepository
) : ViewModel() {
    private var _totalMenuList: MutableMap<Menu, CartItem>? = null

//    Menu
    private val _menuList = MutableLiveData<Map<Menu, CartItem>>()
    val menuList = _menuList as LiveData<Map<Menu, CartItem>>

    fun refreshMenuList() {
        viewModelScope.launch {
            _totalMenuList = appRepository.getMenusWithCartItem()
            _menuList.value = _totalMenuList ?: mapOf()
        }
    }

    fun filterMenuList(search: String, filter: String) {
        _menuList.value = _totalMenuList?.filter {
            it.key.name.contains(search, true) && it.key.type.contains(filter, true)
        }
    }

    fun addToCart(menu: Menu) {
        var cartItem = _totalMenuList!!.getOrDefault(menu, CartItem(menu.name, 0))
        cartItem.quantity += 1
        appRepository.updateCartItem(cartItem)
        _totalMenuList!![menu] = cartItem
    }

    fun removeFromCart(menu: Menu) {
        var cartItem = _totalMenuList!![menu]
        cartItem!!.quantity -= 1
        appRepository.updateCartItem(cartItem!!)
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