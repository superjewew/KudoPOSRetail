package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        //val productId = intent.getLongExtra(INTENT_PRODUCT_ID,1)
        val productName = intent.getStringExtra(INTENT_PRODUCT_NAME)
        val productImage = intent.getStringExtra(INTENT_PRODUCT_IMAGE)
        //val productDescription = intent.getStringExtra(INTENT_PRODUCT_COMMISSION)
        val commission = intent.getDoubleExtra(INTENT_PRODUCT_COMMISSION,0.0)
        val price = intent.getDoubleExtra(INTENT_PRODUCT_PRICE,0.0)
        val productCommission = "Komisi @ ${NumberUtils.formatPrice(commission)}"
        val productPrice = NumberUtils.formatPrice(price)

        item_cart_tv_item_name.text = productName
        Picasso.with(this)
                .load(productImage)
                .into(item_cart_iv_image)
        item_cart_tv_price.text = productPrice.toString()
        item_cart_tv_commission_details.text  = productCommission.toString()

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

    fun goToCheckout() {
        Snackbar.make(layout_commission_total,"checkout",Snackbar.LENGTH_SHORT).show()
        var builder = SelectPaymentActivity_.intent(this)
        builder.start()
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
