package com.pap.majika.stores

import androidx.room.*
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu

@Dao
interface MenuDao {
    @Query(
        "SELECT * FROM menu " +
                "LEFT JOIN cart_item ON cart_item.name = menu.name"
    )
    fun getAllMenuWithCartItem(): Map<Menu, List<CartItem>>

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
        "JOIN menu ON cart_item.name = menu.name " +
                "WHERE cart_item.quantity > 0"
    )
    fun getAllCartItem(): Map<CartItem, List<Menu>>

    @Delete
    fun deleteCartItem(cartItem: CartItem) : Int

    @Query("DELETE FROM cart_item")
    fun deleteAllCartItem(): Int

    @Upsert(entity = CartItem::class)
    fun updateCartItem(cartItem: CartItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCartItem(cartItem: CartItem) : Long
}