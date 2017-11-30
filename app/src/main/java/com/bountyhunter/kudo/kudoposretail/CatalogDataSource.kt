package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.Product
import rx.Observable

/**
 * Created by norman on 11/28/17.
 */
interface CatalogDataSource {
    fun getProducts(): Observable<List<ProductCatalog>>

    fun getProduct(id: Long): Observable<Product>

    fun saveToLocalDb(catalogs: List<ProductCatalog>)
}