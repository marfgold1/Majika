package com.pap.majika

import android.app.Application
import com.pap.majika.repository.AppRepository
import com.pap.majika.stores.AppStore

class MajikaApp: Application() {
    private lateinit var appRepository: AppRepository

    override fun onCreate() {
        super.onCreate()
        appRepository = AppRepository(
            AppStore.getInstance(applicationContext),
        )
    }

    fun getAppRepository(): AppRepository {
        return appRepository
    }
}