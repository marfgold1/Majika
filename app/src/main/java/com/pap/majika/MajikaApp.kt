package com.pap.majika

import com.pap.majika.repository.AppRepository
import com.pap.majika.stores.AppStore

class MajikaApp: android.app.Application() {
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