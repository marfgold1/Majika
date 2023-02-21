package com.pap.majika.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.stores.AppStore

class AppRepository(
    private val appStore: AppStore,
) {
    private var _menusWithCartItem = MutableLiveData<Map<Menu, CartItem>>(mapOf())
     val menusWithCartItem get() = _menusWithCartItem as LiveData<Map<Menu, CartItem>>


    //    Menu
    suspend fun getMenusWithCartItem() : Map<Menu, CartItem> {
        if (_menusWithCartItem.value == null) {
            _menusWithCartItem.value = loadMenusWithCartItem()
        }
        return menusWithCartItem.value!!
    }
    private suspend fun loadMenusWithCartItem() : Map<Menu, CartItem> {
        val menusWithCartItem = appStore.menuDao().getAllMenuWithCartItem()
        try {
            val response = MajikaApi.getInstance().getMenus()
            var menus = response.data!!
            var oldMenusName = menusWithCartItem.map {
//                name and description
                it.key.name to it.key.description
            }
            var newMenus = menus.filter {
                !oldMenusName.contains(it.name to it.description)
            }

            if (newMenus.isNotEmpty()) {
                appStore.menuDao().updateAllMenu(menus)
                appStore.menuDao().deleteAllCartItem()
                Log.d("Repository", "loadMenusWithCartItem: ${menusWithCartItem.map { it.key.name }}")
                return menus.associateWith {
                    CartItem(it.name, it.description, 0)
                }.toMutableMap()
            }
        } catch (e: Exception) {
            Log.e("MajikaApp", "AppRepository initialization failed", e)
        }
        Log.d("Repository", "menusWithCartItem: ${menusWithCartItem.map { it.key.name }}")
        return menusWithCartItem.map {
            if (it.value.isEmpty()) {
                it.key to CartItem(it.key.name, it.key.description, 0)
            } else {
                it.key to it.value[0]
            }
        }.toMap().toMutableMap()
    }

    suspend fun refreshMenusWithCartItem() {
        _menusWithCartItem.value = loadMenusWithCartItem()
    }


    suspend fun addCartItem(menu: Menu, qty: Int) {
        var cartItem = getMenusWithCartItem().getOrDefault(menu, CartItem(menu.name, menu.description, 0))
        cartItem.quantity += qty
        appStore.menuDao().updateCartItem(cartItem)
        refreshMenusWithCartItem()
    }

    suspend fun removeCartItem(menu: Menu, qty: Int) {
        var cartItem: CartItem = getMenusWithCartItem()[menu] ?: return
        cartItem.quantity -= qty
        if (cartItem.quantity < 0) {
            appStore.menuDao().deleteCartItem(cartItem)
        } else {
            appStore.menuDao().updateCartItem(cartItem)
        }
        refreshMenusWithCartItem()
    }

    suspend fun clearCart() : Int {
        val count = appStore.menuDao().deleteAllCartItem()
        refreshMenusWithCartItem()
        return count
    }
}