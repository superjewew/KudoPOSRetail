package com.bountyhunter.kudo.kudoposretail.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by norman on 11/21/17.
 */
open class CartItem : RealmObject() {
    @PrimaryKey
    var id: Long? = 0
    var mItemId: Long? = 0
    var mItemName: String? = ""
    var mItemQuantity: Int = 0
    var mItemPrice: Long = 0
}