package com.bountyhunter.kudo.kudoposretail

import android.content.Context
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.Product
import com.bountyhunter.kudo.kudoposretail.rxjava.RealmObservable
import io.realm.Realm
import io.realm.RealmResults
import rx.Observable
import rx.functions.Func1

/**
 * Created by norman on 11/28/17.
 */
class CatalogLocalDataSource(val context: Context): CatalogDataSource {

    override fun saveToLocalDb(catalogs: List<ProductCatalog>) {
        Realm.getDefaultInstance().use {
            for(catalog in catalogs) {
                it.executeTransaction {
                    val item = realmFromCatalog(catalog)
                    it.copyToRealmOrUpdate(item)
                }
            }
        }
    }

    override fun getProduct(id: Long): Observable<Product> {
        return RealmObservable.prepare(context, Func1 { realm ->
            realm.where(Product::class.java).equalTo("id", id).findFirst()
        })
    }

    override fun getProducts(query: String): List<ProductCatalog> {
        return ArrayList()
    }

    override fun getProducts(): Observable<List<ProductCatalog>> {
        return RealmObservable.results(context, Func1<Realm, RealmResults<Product>> { realm ->
            realm.where(Product::class.java).findAll()
        }).map({ products ->
            val catalog = ArrayList<ProductCatalog>(products.size)
            products.mapTo(catalog) { catalogFromRealm(it) }
            catalog
        })
    }

    private fun catalogFromRealm(product: Product?): ProductCatalog {
        val catalog = ProductCatalog()
        catalog.id = product?.id!!
        catalog.name = product.name
        catalog.price = product.price
        catalog.commission = product.commission
        catalog.description = product.description
        catalog.image = product.image
        catalog.stock = product.stock
        return catalog
    }

    private fun realmFromCatalog(catalog: ProductCatalog): Product {
        val item = Product()
        item.id = catalog.id
        item.name = catalog.name
        item.description = catalog.description
        item.price = catalog.price
        item.commission = catalog.commission
        item.image = catalog.image
        item.stock = catalog.stock
        item.barcode = catalog.barcode
        return item
    }

}