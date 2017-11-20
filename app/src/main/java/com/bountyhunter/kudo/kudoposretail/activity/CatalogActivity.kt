package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.rxjava.CatalogManager
import com.bountyhunter.kudo.kudoposretail.ui.GridCatalogDecoration
import com.bountyhunter.kudo.kudoposretail.ui.ProductCatalogAdapter
import kotlinx.android.synthetic.main.activity_catalog.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


class CatalogActivity : AppCompatActivity() {

    val column = 2

    private val compositeSubcribtion : CompositeSubscription = CompositeSubscription()

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

    override fun onResume() {
        super.onResume()
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
        val intent = CartActivity.newIntent(this,product)
        startActivity(intent)
    }

    fun goToVoid() {
        val intent = VoidActivity.newIntent(this)
        startActivity(intent)
    }

    fun goToSettlement() {

    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CatalogActivity::class.java)
            return intent
        }
    }
}
