package com.bountyhunter.kudo.kudoposretail.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by adrian on 11/18/17.
 */
class RestAPI {

    private val hackaidoApi : HackaidoApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://private-9d06dc-hackaido1.apiary-mock.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        hackaidoApi = retrofit.create(HackaidoApi::class.java)
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        var loginRequest = LoginRequest(email,password)
        return hackaidoApi.login(loginRequest)
    }

    fun void(transNo: String, pin : Int): Call<VoidResponse> {
        var voidRequest = VoidRequest(transNo, pin)
        return hackaidoApi.void(voidRequest)
    }

    fun getProducts() = hackaidoApi.products()
}

