package com.bountyhunter.kudo.kudoposretail.rxjava

import com.bountyhunter.kudo.kudoposretail.api.LoginResponse
import com.bountyhunter.kudo.kudoposretail.api.RestAPI

import rx.Observable
/**
 * Created by adrian on 11/18/17.
 */
class LoginManager {

     fun login(email: String, password: String): Observable<LoginResponse> {
        return Observable.create { subscriber ->
            run {
                val restApi = RestAPI()
                val callResponse = restApi.login(email, password)
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val news = response.body()
                    if (news != null) {
                        subscriber.onNext(news)
                    }
                } else {
                    subscriber.onError(Throwable("Gagal login"))
                }
                subscriber.onCompleted()
            }
        }
    }

}