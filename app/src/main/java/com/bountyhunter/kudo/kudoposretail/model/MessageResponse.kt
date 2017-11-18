package com.bountyhunter.kudo.kudoposretail.model

import com.squareup.moshi.Json

/**
 * Created by adrian on 11/18/17.
 */
class MessageResponse(
        val id: Long,
        val name: String,
        val description: String,
        val price: Double,
        val stock: Int
)
