package com.bountyhunter.kudo.kudoposretail.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_product_catalog.view.*

/**
 * Created by adrian on 11/19/17.
 */
class ProductCatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    fun bindItems(myProductCatalog: ProductCatalog, listener: (ProductCatalog) -> Unit) = with(itemView) {
        itemView.product_name_tv.text = myProductCatalog.name
        Picasso.with(context)
                .load(myProductCatalog.image)
                .into(itemView.product_image_iv)
        itemView.product_price_tv.text = NumberUtils.formatPrice(myProductCatalog.price)
        itemView.prouct_commission_tv.text = "Komisi  ${NumberUtils.formatPrice(myProductCatalog.commission)}"
        itemView.setOnClickListener {
            listener(myProductCatalog)
        }
    }


}