package com.pap.majika.repository

import android.util.Log
import com.pap.majika.api.MajikaApi
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu
import com.pap.majika.stores.AppStore
import retrofit2.Retrofit
import retrofit2.await

class AppRepository(
    private val appStore: AppStore,
    private val retrofitClient: Retrofit
) {
//    Menu
    suspend fun getMenus() : List<Menu> {
        return try {
            val response = retrofitClient.create(MajikaApi::class.java).getMenus()
            var menus = response.await().data!!
            var oldMenus = appStore.menuDao().getAllMenu()
            if (oldMenus != menus) {
                appStore.menuDao().updateAllMenu(menus)
                appStore.menuDao().deleteAllCartItem()
            }
            menus
        } catch (e: Exception) {
            appStore.menuDao().getAllMenu()
        }
    }

    fun updateCartItem(cartItem: CartItem) : Int {
        return if (cartItem.quantity == 0) {
            appStore.menuDao().deleteCartItem(cartItem)
        } else {
            appStore.menuDao().updateCartItem(cartItem)
        }
    }

    fun clearCart() : Int {
        return appStore.menuDao().deleteAllCartItem()
    }

    fun getCartItems() : Map<CartItem, Menu> {
        val cartItems = appStore.menuDao().getAllCartItem()
        return cartItems.map {
            it.key to it.value[0]
        }.toMap()
    }
}