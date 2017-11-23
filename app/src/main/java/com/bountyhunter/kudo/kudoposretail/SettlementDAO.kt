package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.model.Settlement
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by norman on 11/22/17.
 */
class SettlementDAO {

    var mRealm : Realm = Realm.getDefaultInstance()

    fun getAll() : RealmResults<Settlement> = mRealm.where(Settlement::class.java).findAll()

    fun create(transNo : String, mTotalPrice : Long) {
        mRealm.executeTransaction({ realm ->
            val settlement = realm.createObject(Settlement::class.java)
            settlement.transId = "" + transNo
            settlement.price = mTotalPrice
        })
    }

    fun deleteAll() {
        var results = mRealm.where(Settlement::class.java).findAll()
        results.deleteAllFromRealm()
    }
}