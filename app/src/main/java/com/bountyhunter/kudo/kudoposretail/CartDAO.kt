package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.model.CartItem
import io.realm.Realm
import io.realm.RealmResults

/**
 * Created by norman on 11/29/17.
 */
class CartDAO {

    fun getAll(): RealmResults<CartItem>? {
        Realm.getDefaultInstance().use {
            return it.where(CartItem::class.java).findAllAsync()
        }
    }

    fun deleteItem(item: CartItem) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { item.deleteFromRealm() }
        }
    }

    fun addQuantity(item: CartItem) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { item.mItemQuantity++ }
        }
    }

    fun reduceQuantity(item: CartItem) {
        Realm.getDefaultInstance().use {
            it.executeTransaction { item.mItemQuantity-- }
        }
    }
}