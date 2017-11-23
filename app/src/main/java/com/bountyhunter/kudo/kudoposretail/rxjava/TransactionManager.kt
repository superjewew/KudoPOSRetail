package com.bountyhunter.kudo.kudoposretail.rxjava

import com.bountyhunter.kudo.kudoposretail.api.LoginResponse
import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import com.bountyhunter.kudo.kudoposretail.api.TransactionRequest
import rx.Observable
import java.util.*

/**
 * Created by adrian on 11/20/17.
 */
class TransactionManager {

    fun submitTransaction(request: TransactionRequest): rx.Observable<LoginResponse> {
        return Observable.create { subscriber ->
            run {
                val restApi = RestAPI()
                val callResponse = restApi.submitTransaction(request)
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val message = response.body()
                    if (message != null) {
                        subscriber.onNext(message)
                    }
                } else {
                    subscriber.onError(Throwable("Gagal login"))
                }
                subscriber.onCompleted()
            }
        }
    }

}