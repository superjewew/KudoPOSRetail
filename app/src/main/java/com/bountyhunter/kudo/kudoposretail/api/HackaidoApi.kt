package com.bountyhunter.kudo.kudoposretail.api

import com.bountyhunter.kudo.kudoposretail.model.HackaidoBodyResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by adrian on 11/18/17.
 */
interface HackaidoApi {

    @POST("/doLogin")
    fun login(@Field("email") email : String, @Field("password") password : String)
            : Call<HackaidoBodyResponse<String>>


}