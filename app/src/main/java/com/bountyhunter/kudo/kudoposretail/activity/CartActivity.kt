package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.ui.CartItemAdapter
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_cart.*
import kotlin.properties.Delegates


class CartActivity : AppCompatActivity() {

    private var realm: Realm by Delegates.notNull()

    private lateinit var items : RealmResults<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        realm = Realm.getDefaultInstance()

        val realm = Realm.getDefaultInstance()
        items = realm.where(CartItem::class.java).findAllAsync()
        items.addChangeListener(callback)
        initAdapter(items)
        updateTotalPrice()

        shopping_cart_btn_checkout.setOnClickListener {
            goToCheckout()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                // app icon in action bar clicked; goto parent activity.
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private var callback = OrderedRealmCollectionChangeListener {
        _: RealmResults<CartItem>, changeSet: OrderedCollectionChangeSet? ->
        if (changeSet == null) {
                // The first time async returns with an null changeSet.
            } else {
                // Called on every update.
            }
    }

    fun initAdapter(cartItems: RealmResults<CartItem>) {
        rv_cart.layoutManager = LinearLayoutManager(this)
        rv_cart.setHasFixedSize(true)
        rv_cart.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_cart.adapter = CartItemAdapter(cartItems, true,
                { item -> deleteItemFromCart(item) },
                { item -> addItemAmount(item) },
                { item -> reduceItemAmount(item) })
    }

    fun goToCheckout() {
        Snackbar.make(layout_commission_total,"checkout",Snackbar.LENGTH_SHORT).show()
        var builder = SelectPaymentActivity_.intent(this)
        builder.start()
    }

    fun deleteItemFromCart(item : CartItem) {
        realm.executeTransaction {
            item.deleteFromRealm()
            updateTotalPrice()
        }
    }

    fun addItemAmount(item : CartItem) {
        realm.executeTransaction {
            item.mItemQuantity++
            updateTotalPrice()
        }
    }

    fun reduceItemAmount(item: CartItem) {
        realm.executeTransaction {
            if(item.mItemQuantity > 0) {
                item.mItemQuantity--
                updateTotalPrice()
            }
        }
    }

    fun updateTotalPrice() {
        shopping_cart_tv_commission_total.text = NumberUtils.formatPrice(calculateTotalPrice())
    }

    fun calculateTotalPrice() : Double {
        var total = 0.0
        for(item in items) {
            total += item.mItemPrice * item.mItemQuantity
        }
        return total
    }

    companion object {

        private val INTENT_PRODUCT_ID = "product_id"
        private val INTENT_PRODUCT_NAME = "product_name"
        private val INTENT_PRODUCT_IMAGE = "product_image"
        private val INTENT_PRODUCT_DESCRIPTION = "product_description"
        private val INTENT_PRODUCT_COMMISSION = "product_commission"
        private val INTENT_PRODUCT_PRICE = "product_price"

        fun newIntent(context: Context, productCatalog: ProductCatalog): Intent {
            val intent = Intent(context, CartActivity::class.java)
            intent.putExtra(INTENT_PRODUCT_ID,productCatalog.id)
            intent.putExtra(INTENT_PRODUCT_IMAGE,productCatalog.image)
            intent.putExtra(INTENT_PRODUCT_NAME, productCatalog.name)
            intent.putExtra(INTENT_PRODUCT_DESCRIPTION, productCatalog.description)
            intent.putExtra(INTENT_PRODUCT_COMMISSION, productCatalog.commission)
            intent.putExtra(INTENT_PRODUCT_PRICE, productCatalog.price)
            return intent
        }
    }
}
