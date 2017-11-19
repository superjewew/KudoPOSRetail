package com.bountyhunter.kudo.kudoposretail.api

import com.google.gson.annotations.SerializedName

/**
 * Created by adrian on 11/18/17.
 */
class LoginRequest (
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String
)
class LoginResponse(
        val code: Int,
        val message: String
)
class ProductResponse(
        val id: Long,
        val name: String,
        val description: String,
        val price: Double,
        val stock: Int
)
class VoidRequest (
        @SerializedName("id") val transNumber: String,
        @SerializedName("pin") val pin : Int
)
class VoidResponse (
        val code: Int,
        val message: String
)
