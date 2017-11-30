package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.bountyhunter.kudo.kudoposretail.CatalogLocalDataSource
import com.bountyhunter.kudo.kudoposretail.CatalogRemoteDataSource
import com.bountyhunter.kudo.kudoposretail.CatalogRepository
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.rxjava.CatalogManager
import com.bountyhunter.kudo.kudoposretail.ui.ProductCatalogAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_catalog.*
import org.jetbrains.anko.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription


class CatalogActivity : AppCompatActivity() {

    val column = 2

    val context : Context = this

    private val compositeSubcribtion : CompositeSubscription = CompositeSubscription()

    private val catalogManager by lazy {
        CatalogManager()
    }

    private val catalogRepo by lazy {
        CatalogRepository(CatalogLocalDataSource(this), CatalogRemoteDataSource())
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
                if(search_tv.text.isNotEmpty()) {
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
        })

        val disposableRealm = catalogRepo.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {   // on next
                            data -> initAdapter(data)
                            catalogRepo.saveToLocalDb(data)
                        },
                        {   // on error
                            error -> toast(error.message.toString())
                        }
                )

        compositeSubcribtion.add(disposableRealm)
    }

    fun initAdapter(productRespons: List<ProductCatalog>) {
        rv_products_catalog.adapter = ProductCatalogAdapter(ArrayList(productRespons)) { productResponse ->
            Toast.makeText(this, productResponse.name, Toast.LENGTH_SHORT).show()
            onProductClicked(productResponse)
        }
    }

    private fun onProductClicked(product : ProductCatalog) {
        tryAddProductToCart(product)
        goToCart()
    }

    private fun tryAddProductToCart(product : ProductCatalog) {
        val thread = Thread(Runnable {
            val realmIns = Realm.getDefaultInstance()
            realmIns.use { realm ->
                addToCart(realm, product)
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

    private fun goToCart() {
        val intent = CartActivity.newIntent(this)
        startActivity(intent)
    }

    private fun goToVoid() {
        val intent = VoidActivity.newIntent(this)
        startActivity(intent)
    }

    private fun goToSettlement() {
        val intent = SettlementActivity.newIntent(this)
        startActivity(intent)
    }

    private fun goToHistory() {
        val intent = TransactionListActivity.newIntent(this)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_void -> goToVoid()
            R.id.menu_settlement -> goToSettlement()
            R.id.menu_cart -> goToCart()
            R.id.menu_history -> goToHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, CatalogActivity::class.java)
            return intent
        }
    }
}
