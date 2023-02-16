package com.pap.majika.repository

import android.util.Log
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.stores.AppStore
import retrofit2.await

class AppRepository(
    private val appStore: AppStore,
) {
//    Menu
    suspend fun getMenusWithCartItem() : MutableMap<Menu, CartItem> {
        val menusWithCartItem = appStore.menuDao().getAllMenuWithCartItem()
        try {
            val response = MajikaApi.getInstance().getMenus()
            var menus = response.await().data!!
            var oldMenusName = menusWithCartItem.map {
                it.key.name
            }
            var newMenus = menus.filter {
                !oldMenusName.contains(it.name)
            }

            if (newMenus.isNotEmpty()) {
                appStore.menuDao().updateAllMenu(menus)
                appStore.menuDao().deleteAllCartItem()
                return menus.associateWith {
                    CartItem(it.name, 0)
                }.toMutableMap()
            }
        } catch (e: Exception) {
            Log.e("MajikaApp", "AppRepository initialization failed", e)
        }
        return menusWithCartItem.map {
            if (it.value.isEmpty()) {
                it.key to CartItem(it.key.name, 0)
            } else {
                it.key to it.value[0]
            }
        }.toMap().toMutableMap()
    }

    fun updateCartItem(cartItem: CartItem) {
        if (cartItem.quantity == 0) {
            appStore.menuDao().deleteCartItem(cartItem)
        } else {
            appStore.menuDao().updateCartItem(cartItem)
        }
    }

    fun clearCart() : Int {
        return appStore.menuDao().deleteAllCartItem()
    }

    fun getCartItems() : MutableMap<Menu, CartItem> {
        val cartItems = appStore.menuDao().getAllCartItem()
        return cartItems.map {
            it.value[0] to it.key
        }.toMap().toMutableMap()
    }
}