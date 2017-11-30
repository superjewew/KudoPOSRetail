package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bountyhunter.kudo.kudoposretail.MposPrinter
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.receipt.PlnReceipt
import kotlinx.android.synthetic.main.list_item_history.*

class TransactionListActivity : AppCompatActivity() {

    private val mPrinter: MposPrinter = MposPrinter.getInstance(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_list)

        supportActionBar?.setHomeButtonEnabled(true)

        print_button.setOnClickListener{
            val receipt = PlnReceipt("201711301883")
            mPrinter.setReceipt(receipt)
            mPrinter.print()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            // app icon in action bar clicked; goto parent activity.
            this.finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, TransactionListActivity::class.java)
    }
}
