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

    fun fromProduct(product: Product) {
        mItemId = product.id
        mItemName = product.name
        mItemQuantity = 1
        mItemPrice = product.price
        mItemImage = product.image
        mItemStock = product.stock
    }
}