package com.bountyhunter.kudo.kudoposretail.rxjava

import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import com.bountyhunter.kudo.kudoposretail.api.VoidResponse
import rx.Observable

/**
 * Created by norman on 11/19/17.
 */
class VoidManager {

    fun void(transactionNumber: String) : Observable<VoidResponse> {
        return Observable.create { subscriber ->
            run {
                val restAPI = RestAPI()
                val callResponse = restAPI.void(transactionNumber)
                val response = callResponse.execute()
                if(response.isSuccessful) {

                } else {
                    subscriber.onError(Throwable("Gagal membatalkan transaksi"))
                }
                subscriber.onCompleted()
            }
        }
    }
}