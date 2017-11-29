package com.bountyhunter.kudo.kudoposretail.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.bountyhunter.kudo.kudoposretail.CartDAO
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.ui.CartItemAdapter
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_cart.*
import org.jetbrains.anko.toast
import kotlin.properties.Delegates


class CartActivity : AppCompatActivity() {

    private val paymentRequestCode = 0

    private var realm: Realm by Delegates.notNull()

    private lateinit var items : RealmResults<CartItem>

    private val dao = CartDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        realm = Realm.getDefaultInstance()

        shopping_cart_btn_checkout.setOnClickListener {
            when {
                items.size == 0 -> toast("Keranjang belanja kosong")
                else -> goToCheckout()
            }
        }

        shopping_cart_btn_continue_shopping.setOnClickListener {
            finish()
        }

        fab_camera.setOnClickListener {
            val intent = ScannerActivity.newIntent(this)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        items = dao.getAll()!!
        initAdapter(items)
        updateTotalPrice()
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
        if(requestCode == paymentRequestCode) {
            if(resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    private fun initAdapter(cartItems: RealmResults<CartItem>) {
        rv_cart.layoutManager = LinearLayoutManager(this)
        rv_cart.setHasFixedSize(true)
        rv_cart.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        rv_cart.adapter = CartItemAdapter(cartItems, true,
                { item -> deleteItemFromCart(item) },
                { item -> addItemAmount(item) },
                { item -> reduceItemAmount(item) })
    }

    private fun goToCheckout() {
        val builder = SelectPaymentActivity_.intent(this)
        builder.startForResult(paymentRequestCode)
    }

    private fun deleteItemFromCart(item : CartItem) {
        dao.deleteItem(item)
        updateTotalPrice()
    }

    private fun addItemAmount(item : CartItem) {
        dao.addQuantity(item)
        updateTotalPrice()
    }

    private fun reduceItemAmount(item: CartItem) {
        if(item.mItemQuantity > 0) {
            dao.reduceQuantity(item)
            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        shopping_cart_tv_commission_total.text = NumberUtils.formatPrice(calculateTotalPrice())
    }

    private fun calculateTotalPrice() : Double = items.sumByDouble { it.mItemPrice * it.mItemQuantity }

    companion object {

        fun newIntent(context: Context): Intent = Intent(context, CartActivity::class.java)

    }
}
