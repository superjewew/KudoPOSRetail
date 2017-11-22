package com.bountyhunter.kudo.kudoposretail.api

import retrofit2.Call
import retrofit2.http.*

/**
 * Created by adrian on 11/18/17.
 */
interface HackaidoApi {

    @POST("doLogin")
    fun login(@Body loginRequest: LoginRequest)
            : Call<LoginResponse>

    @POST("void")
    fun void(@Body voidRequest: VoidRequest) : Call<VoidResponse>

    @GET("products/{id}")
    fun products( @Path(value = "id", encoded = true) id : Int) : Call<ProductResponse>

    @GET("products")
    fun products( ) : Call<ProductResponse>

    @POST("submitTransaction")
    fun submitTransaction(@Body transactionRequest: TransactionRequest) : Call<LoginResponse>
}