package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.model.CartItem
import io.realm.Realm

/**
 * Created by norman on 11/23/17.
 */
class CartItemDao {

    var mRealm : Realm = Realm.getDefaultInstance()

    fun deleteAll() {
        var results = mRealm.where(CartItem::class.java).findAll()
        mRealm.executeTransaction { results.deleteAllFromRealm() }
    }
}