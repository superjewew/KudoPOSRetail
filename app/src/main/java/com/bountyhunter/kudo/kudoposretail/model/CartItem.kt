package com.bountyhunter.kudo.kudoposretail.model

import io.realm.RealmObject

/**
 * Created by norman on 11/21/17.
 */
open class CartItem : RealmObject() {
    var mItemId: Long? = 0
    var mItemName: String? = ""
    var mItemQuantity: Int = 0
    var mItemPrice: Double = 0.0
    var mItemImage: String = "No Image"
    var mItemStock: Int = 10
}