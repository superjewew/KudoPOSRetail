package com.bountyhunter.kudo.kudoposretail.api

import com.bountyhunter.kudo.kudoposretail.model.HackaidoBodyResponse
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by adrian on 11/18/17.
 */
class RestAPI {

    private val hackaidoApi : HackaidoApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://private-9d06dc-hackaido1.apiary-mock.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        hackaidoApi = retrofit.create(HackaidoApi::class.java)
    }

    fun login(email: String, password: String): Call<HackaidoBodyResponse<String>> {
        return hackaidoApi.login(email, password)
    }
}

