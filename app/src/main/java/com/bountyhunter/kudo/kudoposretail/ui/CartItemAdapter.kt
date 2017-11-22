package com.bountyhunter.kudo.kudoposretail.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import com.squareup.picasso.Picasso
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.cart_item_layout.view.*

/**
 * Created by norman on 11/21/17.
 */
class CartItemAdapter(data: OrderedRealmCollection<CartItem>?,
                      autoUpdate: Boolean,
                      private val deleteListener: (CartItem) -> Unit,
                      private val addAmountListener: (CartItem) -> Unit,
                      private val reduceAmountdeleteListener: (CartItem) -> Unit)
    : RealmRecyclerViewAdapter<CartItem, CartItemViewHolder>(data, autoUpdate) {

    override fun onBindViewHolder(holder: CartItemViewHolder?, position: Int) {
        holder?.bindItems(data!![position], deleteListener, addAmountListener, reduceAmountdeleteListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CartItemViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartItemViewHolder(v)
    }

    override fun getItemCount(): Int = data!!.size

}

class CartItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun bindItems(item: CartItem, listener: (CartItem) -> Unit,
                  addAmountListener: (CartItem) -> Unit,
                  reduceAmountdeleteListener: (CartItem) -> Unit) = with(itemView) {
        itemView.item_cart_tv_item_name.text = item.mItemName
        itemView.item_cart_tv_price.text = NumberUtils.formatPrice(item.mItemPrice)
        itemView.item_cart_tv_quantity.text = item.mItemQuantity.toString()
        Picasso.with(context)
                .load(item.mItemImage)
                .into(itemView.item_cart_iv_image)
        itemView.item_cart_btn_delete.setOnClickListener { listener(item) }
        itemView.item_cart_iv_subs_qty.setOnClickListener { reduceAmountdeleteListener(item) }
        itemView.item_cart_iv_add_qty.setOnClickListener { addAmountListener(item) }
    }
}
