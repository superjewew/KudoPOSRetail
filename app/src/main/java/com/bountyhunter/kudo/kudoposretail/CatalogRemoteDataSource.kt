package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.api.RestAPI
import com.bountyhunter.kudo.kudoposretail.model.Product
import rx.Observable

/**
 * Created by norman on 11/28/17.
 */
class CatalogRemoteDataSource: CatalogDataSource {
    override fun saveToLocalDb(products: List<ProductCatalog>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProduct(id: Long): Observable<Product> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProducts(): Observable<List<ProductCatalog>> {
        return Observable.create { subscriber ->
            run {
                val restApi = RestAPI()
                val callResponse = restApi.getProducts()
                val response = callResponse.execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        subscriber.onNext(body.message.data!!)
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