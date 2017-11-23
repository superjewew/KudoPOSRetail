package com.bountyhunter.kudo.kudoposretail.model

import io.realm.RealmObject

/**
 * Created by norman on 11/22/17.
 */
open class Settlement : RealmObject() {
    var transId : String = ""
    var price : Long = 0
}