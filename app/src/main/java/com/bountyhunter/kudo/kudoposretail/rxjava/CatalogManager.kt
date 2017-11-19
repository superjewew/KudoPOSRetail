package com.bountyhunter.kudo.kudoposretail.rxjava

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import rx.Observable

/**
 * Created by adrian on 11/19/17.
 */
class CatalogManager {
    fun getProducts() : Observable<List<ProductCatalog>> {
        return Observable.create{ subscriber ->
            run {
                val restApi = RestAPI()
                val callResponse = restApi.getProducts()
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        subscriber.onNext(response.message.data!!)
                    } else {
                        subscriber.onError(Throwable("Product is null"))
                    }
                } else {
                    subscriber.onError(Throwable("Gagal login"))
                }
                subscriber.onCompleted()
            }
        }
    }
}