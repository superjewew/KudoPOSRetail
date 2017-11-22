package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.rxjava.CatalogManager
import com.bountyhunter.kudo.kudoposretail.ui.ProductCatalogAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_catalog.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlin.properties.Delegates


class CatalogActivity : AppCompatActivity() {

    val column = 2

    val context : Context = this

    private val compositeSubcribtion : CompositeSubscription = CompositeSubscription()

    private var realm: Realm by Delegates.notNull()

    private val catalogManager by lazy {
        CatalogManager()
    }

    override fun onPause() {
        super.onPause()
        if(compositeSubcribtion.hasSubscriptions()) {
            compositeSubcribtion.clear()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_catalog)

        rv_products_catalog.layoutManager =
                GridLayoutManager(this, column)


        search_tv.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(search_tv.text.length != 0) {
                    val id : Int = search_tv.text.toString().toInt()
                    val disposable = catalogManager.getProductById(id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe (
                                    {
                                        data -> initAdapter(data)
                                    },
                                    {
                                        e -> Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                                    }
                            )
                    compositeSubcribtion.add(disposable)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        }
        )

        //rv_products_catalog.addItemDecoration(GridCatalogDecoration(12))

//        var alMyProductResponse = ArrayList<ProductCatalog>()
//
//        alMyProductResponse.add(ProductCatalog(description = "Item 1"
//        ,id = 1,name = "Item 1",price=12000.0,image = "123123",stock = 1))
//
//        alMyProductResponse.add(ProductCatalog(description = "Item 1"
//                ,id = 1,name = "Item 1",price=12000.0,image = "123123",stock = 1))
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//        alMyProductResponse.add(ProductCatalog())
//
//        rv_products_catalog.adapter = ProductCatalogAdapter(alMyProductResponse) {
//            productResponse -> Toast.makeText(this, productResponse.name + " is clicked"
//                ,Toast.LENGTH_SHORT).show()
//        }
        val disposable = catalogManager.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        {
                            data -> initAdapter(data)
                        },
                        {
                            e -> Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                )
        compositeSubcribtion.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_void -> goToVoid()
            R.id.menu_settlement -> goToSettlement()
        }
        return super.onOptionsItemSelected(item)
    }

    fun initAdapter(productRespons: List<ProductCatalog>) {
        rv_products_catalog.adapter = ProductCatalogAdapter(ArrayList(productRespons)) { productResponse ->
            Toast.makeText(this, productResponse.name, Toast.LENGTH_SHORT).show()
            goToCart(productResponse)
        }
    }

    fun goToCart(product : ProductCatalog) {
        tryAddProductToCart(product)
        val intent = CartActivity.newIntent(this,product)
        startActivity(intent)
    }

    fun goToVoid() {
        val intent = VoidActivity.newIntent(this)
        startActivity(intent)
    }

    fun goToSettlement() {

    }

    fun tryAddProductToCart(product : ProductCatalog) {
        val thread = Thread(Runnable {
            val realm = Realm.getDefaultInstance()
            try {
                // ... Use the Realm instance ...
                addToCart(realm, product)
            } finally {
                realm.close()
            }
        })

        thread.start()
    }

    private fun addToCart(realm: Realm, product : ProductCatalog) {
        realm.executeTransaction {
            // Add a person
            val item = realm.createObject(CartItem::class.java)
            item.mItemId = product.id
            item.mItemName = product.name
            item.mItemPrice = product.price
            item.mItemQuantity = 1
            item.mItemImage = product.image
            item.mItemStock = product.stock
        }
    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CatalogActivity::class.java)
            return intent
        }
    }
}
