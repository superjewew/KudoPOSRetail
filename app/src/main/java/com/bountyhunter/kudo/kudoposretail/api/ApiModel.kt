package com.bountyhunter.kudo.kudoposretail.api

import com.google.gson.annotations.SerializedName

/**
 * Created by adrian on 11/18/17.
 */
data class LoginRequest (
        @SerializedName("email") val email: String,
        @SerializedName("password") val password: String
)
data class LoginResponse(
        val code: Int,
        val message: String
)
data class ProductResponse(
        val code: Int,
        val message: DataResponse)

data class DataResponse(
    val data: List<ProductCatalog>
)

data class ProductCatalog(
        val id: Long = 0,
        val name: String = "No Name",
        val description: String = "Desciprtion",
        val price: Double = 0.0,
        val stock: Int = 0,
        val commission: Double = 0.0,
        val image: String = "No Image")

class VoidRequest (
        @SerializedName("id") val transNumber: String,
        @SerializedName("pin") val pin : Int
)
class VoidResponse (
        val code: Int,
        val message: String
)
