package com.bountyhunter.kudo.kudoposretail.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by norman on 11/28/17.
 */
open class Product: RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name: String = "No Name"
    var description: String = "Desciprtion"
    var price: Double = 0.0
    var stock: Int = 0
    var commission: Double = 0.0
    var image: String = "No Image"
    var barcode: String = ""
}