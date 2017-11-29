package com.bountyhunter.kudo.kudoposretail.api

import com.google.gson.annotations.SerializedName
import java.util.*

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
        var id: Long = 0,
        var name: String = "No Name",
        var description: String = "Desciprtion",
        var price: Double = 0.0,
        var stock: Int = 0,
        var commission: Double = 0.0,
        var image: String = "No Image")

class VoidRequest (
        @SerializedName("id") val transNumber: String
)
class VoidResponse (
        val code: Int,
        val message: String
)

data class TransactionRequest (
    val branch_id: Int = 1,
    val method: String = "cash",
    val id: String = "201792827273",
    val data: List<Transaction> = Arrays.asList(Transaction(5,3), Transaction(6,1))
)

data class Transaction (
        val id: Long = 5,
        val qty: Int = 13
)
