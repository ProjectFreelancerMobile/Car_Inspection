package com.car_inspection.data.local.database

import com.car_inspection.app.Constants
import com.car_inspection.data.local.prefs.PreferenceManager
import com.orhanobut.logger.Logger
import io.realm.Realm
import io.realm.RealmConfiguration

@Suppress("ReplaceCallWithBinaryOperator")
/**
 * Created by ToanDev on 07/06/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class RealmManager : RepositoryData {
    private val mRealmConfig: RealmConfiguration = RealmConfiguration.Builder()
            .name(Constants.DB_Realm)
            .deleteRealmIfMigrationNeeded()
            .migration(Migration())
            .schemaVersion(Constants.RealmVersion)
            .build()

    init {
        Realm.setDefaultConfiguration(mRealmConfig)
    }

    override fun clearAll() {
        PreferenceManager.clear()
    }


    override fun closeRealm() {
        Realm.getDefaultInstance().apply {
            if (!isClosed) {
                Logger.e("closeRealm")
                close()
            }
        }
    }

}
