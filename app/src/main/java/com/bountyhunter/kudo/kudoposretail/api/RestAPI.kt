package com.bountyhunter.kudo.kudoposretail.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by adrian on 11/18/17.
 */
class RestAPI {
//0522
    val hackaidoApi: HackaidoApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://private-9d06dc-hackaido1.apiary-mock.com/")
//                .baseUrl("http://192.168.43.113:443/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        hackaidoApi = retrofit.create(HackaidoApi::class.java)
    }

    fun getInstance(): HackaidoApi {
        if (hackaidoApi != null) {
            return hackaidoApi
        } else {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://private-9d06dc-hackaido1.apiary-mock.com/")
//                    .baseUrl("http://192.168.43.113:443/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(HackaidoApi::class.java)
        }
    }

    fun login(email: String, password: String): Call<LoginResponse> {
        var loginRequest = LoginRequest(email, password)
        return hackaidoApi.login(loginRequest)
    }

    fun void(transNo: String): Call<VoidResponse> {
        var voidRequest = VoidRequest(transNo)
        return hackaidoApi.void(voidRequest)
    }

    fun getProducts() = hackaidoApi.products()

    fun getProducts(id : Int) = hackaidoApi.products(id)

    fun submitTransaction(transactionRequest: TransactionRequest) = hackaidoApi.submitTransaction(transactionRequest)
}

