package com.bountyhunter.kudo.kudoposretail

import android.app.Application
import io.realm.Realm

/**
 * Created by norman on 11/22/17.
 */
class PosApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}