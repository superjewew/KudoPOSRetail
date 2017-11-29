package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.model.Settlement
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by norman on 11/22/17.
 */
class SettlementDAO {

    val STATUS_VOID = "VOID"

    var mRealm : Realm = Realm.getDefaultInstance()

    fun getAll() : RealmResults<Settlement> = mRealm.where(Settlement::class.java).findAll()

    fun create(transNo: String, mTotalPrice: Double) {
        mRealm.executeTransaction({ realm ->
            val settlement = realm.createObject(Settlement::class.java)
            settlement.transId = "" + transNo
            settlement.price = mTotalPrice
        })
    }

    fun deleteAll() {
        val results = mRealm.where(Settlement::class.java).findAll()
        mRealm.executeTransaction { results.deleteAllFromRealm() }
    }

    fun updateStatus(transNo: String, status: String): Double {
        var settlementAmount = 0.0

        Realm.getDefaultInstance().use {
            val result = it.where(Settlement::class.java).beginsWith("transId", transNo).findFirst() ?: throw SettlementNotFoundException()
            if(result.status == STATUS_VOID) throw SettlementAlreadyVoidException()
            it.executeTransaction {
                result.status = status
                settlementAmount = result.price
            }
        }

        return settlementAmount
    }

    class SettlementNotFoundException : Throwable()

    class SettlementAlreadyVoidException : Throwable()
}