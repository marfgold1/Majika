package com.pap.majika.stores

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pap.majika.models.CartItem
import com.pap.majika.models.Menu

@Database(entities = [Menu::class, CartItem::class], version = 1)
abstract class AppStore : RoomDatabase() {
    abstract fun menuDao(): MenuDao

    companion object {
        private var instance: AppStore? = null
        fun getInstance(context: Context): AppStore {
            if (instance == null) {
                synchronized(this) {
//                    Get app context
                    instance = Room.databaseBuilder(
                        context,
                        AppStore::class.java,
                        "app_store"
                    ).allowMainThreadQueries().build()
                }
            }
            return instance!!
        }
    }
}