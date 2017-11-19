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
        val code: Int,
        val message: DataResponse)

class DataResponse(
    val data: List<ProductCatalog>
)
class ProductCatalog(
        val id: Long = 0,
        val name: String = "No Name",
        val description: String = "Desciprtion",
        val price: Double = 0.0,
        val stock: Int = 0,
        val image: String = "No Image") {
}
