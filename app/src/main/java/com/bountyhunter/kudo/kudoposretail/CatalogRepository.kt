package com.bountyhunter.kudo.kudoposretail

import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.Product
import io.realm.Realm
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by norman on 11/28/17.
 */
class CatalogRepository(localDataSource: CatalogDataSource, remoteDataSource: CatalogDataSource) : CatalogDataSource {

    private val local = localDataSource
    private val remote = remoteDataSource

    override fun getProduct(id: Long): Observable<Product> = local.getProduct(id)

    override fun saveToLocalDb(products: List<ProductCatalog>) {
        local.saveToLocalDb(products)
    }

    override fun getProducts(): Observable<List<ProductCatalog>> {
        val cached = local.getProducts()

        val observable = remote.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map { products -> saveToLocalDb(products) }
                .observeOn(AndroidSchedulers.mainThread())
                .map { getProductsFromRealm() }

        return Observable.merge(cached, observable)
    }

    private fun getProductsFromRealm() : List<ProductCatalog> {
        return Realm.getDefaultInstance().use {
            val catalogs = ArrayList<ProductCatalog>()
            val result = it.where(Product::class.java).findAll()
            result.mapTo(catalogs) { mapProductToCatalog(it) }
            catalogs
        }
    }

    private fun mapProductToCatalog(product: Product): ProductCatalog {
        val catalog = ProductCatalog()
        catalog.id = product.id
        catalog.name = product.name
        catalog.description = product.description
        catalog.price = product.price
        catalog.commission = product.commission
        catalog.stock = product.stock
        catalog.image = product.image
        catalog.barcode = product.barcode
        return catalog
    }

}