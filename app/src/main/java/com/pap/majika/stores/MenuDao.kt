package com.pap.majika.stores

import androidx.room.*
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu

@Dao
interface MenuDao {
    @Query("SELECT * FROM menu")
    fun getAllMenu(): List<Menu>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMenu(menus: List<Menu>) : List<Long>

    @Query("DELETE FROM menu")
    fun deleteAllMenu(): Int

    @Transaction
    fun updateAllMenu(menus: List<Menu>) {
        deleteAllMenu()
        deleteAllCartItem()
        insertAllMenu(menus)
    }

    @Query(
        "SELECT * FROM cart_item " +
        "JOIN menu ON cart_item.name = menu.name"
    )
    fun getAllCartItem(): Map<CartItem, List<Menu>>

    @Delete
    fun deleteCartItem(cartItem: CartItem) : Int

    @Query("DELETE FROM cart_item")
    fun deleteAllCartItem(): Int

    @Update
    fun updateCartItem(cartItem: CartItem) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(cartItem: CartItem) : Long
}