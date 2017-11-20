package com.bountyhunter.kudo.kudoposretail.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by adrian on 11/18/17.
 */
interface HackaidoApi {

    @POST("doLogin")
    fun login(@Body loginRequest: LoginRequest)
            : Call<LoginResponse>

    @POST("doVoid")
    fun void(@Body voidRequest: VoidRequest) : Call<VoidResponse>

    @GET("products")
    fun products() : Call<ProductResponse>
}