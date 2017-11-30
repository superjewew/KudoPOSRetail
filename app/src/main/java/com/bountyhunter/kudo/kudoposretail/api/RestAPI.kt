package com.bountyhunter.kudo.kudoposretail.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by adrian on 11/18/17.
 */
class RestAPI {
//0522

    companion object {
        val mock = "http://private-9d06dc-hackaido1.apiary-mock.com/"
        lateinit  var hackaidoApi: HackaidoApi

        fun changeBaseUrl(url : String) {
            try {
                var retrofit = Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                hackaidoApi = retrofit.create(HackaidoApi::class.java)
            } catch (e : Exception) {
                changeMock()
            }

        }

        fun changeMock() {
            var retrofit = Retrofit.Builder()
                    .baseUrl(mock)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            hackaidoApi = retrofit.create(HackaidoApi::class.java)
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

