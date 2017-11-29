package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.Product
import rx.Observable

/**
 * Created by norman on 11/28/17.
 */
class CatalogRepository(localDataSource: CatalogDataSource, remoteDataSource: CatalogDataSource) : CatalogDataSource {

    val local = localDataSource
    val remote = remoteDataSource

    override fun getProduct(id: Long): Observable<Product> {
        return local.getProduct(id)
    }

    override fun saveToLocalDb(products: List<ProductCatalog>) {
        local.saveToLocalDb(products)
    }

    override fun getProducts(): Observable<List<ProductCatalog>> {
        val cached = local.getProducts()

        val observable = remote.getProducts()
        observable.mergeWith(cached)

        return observable
    }

}