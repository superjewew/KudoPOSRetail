package com.bountyhunter.kudo.kudoposretail.rxjava

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import rx.Observable
import rx.Subscriber

/**
 * Created by adrian on 11/19/17.
 */
class CatalogManager {
    fun getProducts(): Observable<List<ProductCatalog>> {
        return Observable.create { subscriber ->
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

    fun getProductById(id: Int): Observable<List<ProductCatalog>> {
        return Observable.create { subscribe ->
            run {
                val restApi = RestAPI()
                val callResponse = restApi.getProducts(id)
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val response = response.body()
                    if (response != null) {
                        subscribe.onNext(response.message.data!!)
                    } else {
                        subscribe.onError(Throwable("Error find product"))
                    }
                } else {
                    subscribe.onError(Throwable("Produk tidak ditermukan"))
                }
                subscribe.onCompleted()
            }
        }
    }
}