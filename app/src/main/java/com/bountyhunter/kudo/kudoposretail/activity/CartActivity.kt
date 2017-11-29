package com.bountyhunter.kudo.kudoposretail.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.widget.Toast
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.ui.CartItemAdapter
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_cart.*
import org.jetbrains.anko.toast
import kotlin.properties.Delegates


class CartActivity : AppCompatActivity() {

    val PAYMENT_REQUEST = 0

    private var realm: Realm by Delegates.notNull()

    private lateinit var items : RealmResults<CartItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        realm = Realm.getDefaultInstance()

        shopping_cart_btn_checkout.setOnClickListener {
            when {
                items.size != 0 -> goToCheckout()
                else -> toast("Keranjang belanja kosong")
            }
        }

        shopping_cart_btn_continue_shopping.setOnClickListener {
            finish()
        }

        fab_camera.setOnClickListener { view ->
            val intent = ScannerActivity.newIntent(this)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val realm = Realm.getDefaultInstance()
        items = realm.where(CartItem::class.java).findAllAsync()
        items.addChangeListener(callback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            // app icon in action bar clicked; goto parent activity.
            this.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PAYMENT_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    private var callback = RealmChangeListener<RealmResults<CartItem>> { element ->
        initAdapter(element)
        updateTotalPrice()
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
        var builder = SelectPaymentActivity_.intent(this)
        builder.startForResult(PAYMENT_REQUEST)
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

        fun newIntent(context: Context): Intent = Intent(context, CartActivity::class.java)

    }
}
