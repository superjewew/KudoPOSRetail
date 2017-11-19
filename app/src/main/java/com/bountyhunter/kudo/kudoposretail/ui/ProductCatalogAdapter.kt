package com.bountyhunter.kudo.kudoposretail.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.api.ProductCatalog

/**
 * Created by adrian on 11/19/17.
 */
class ProductCatalogAdapter(
        val myProductCatalogList: ArrayList<ProductCatalog>,
        val listener: (ProductCatalog) -> Unit) : RecyclerView.Adapter<ProductCatalogViewHolder>() {

    override fun getItemCount(): Int = myProductCatalogList.size

    override fun onBindViewHolder(holder: ProductCatalogViewHolder, position: Int) {
        holder.bindItems(myProductCatalogList[position],listener)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductCatalogViewHolder {
        val v = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item_product_catalog, parent, false)
        return ProductCatalogViewHolder(v)
    }

}