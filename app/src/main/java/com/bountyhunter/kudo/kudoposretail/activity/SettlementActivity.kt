package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.model.Settlement
import com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.*
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import com.bountyhunter.kudo.kudoposretail.util.TransactionUtil.determinePaymentMethodFromTransNo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_settlement.*

class SettlementActivity : AppCompatActivity() {

    var mEwalletAmount = 0.0

    var mCardAmount = 0.0

    var mCashAmount = 0.0

    val mRealm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement)

        calculateSettlement()

        print_settlement_button.setOnClickListener {
            mRealm.where(Settlement::class.java).findAllAsync().deleteAllFromRealm()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    private fun calculateSettlement() {
        for(settlement in mRealm.where(Settlement::class.java).findAllAsync()) {
            val transNo = settlement.transId
            val method = determinePaymentMethodFromTransNo(transNo)
            when(method) {
                METHOD_EWALLET -> mEwalletAmount += settlement.price
                METHOD_CARD -> mCardAmount += settlement.price
                METHOD_CASH -> mCashAmount += settlement.price
            }
            updateSettlement()
        }
    }

    private fun updateSettlement() {
        e_wallet_settlement_tv.text = NumberUtils.formatPrice(mEwalletAmount)
        card_settlement_tv.text = NumberUtils.formatPrice(mCardAmount)
        cash_settlement_tv.text = NumberUtils.formatPrice(mCashAmount)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettlementActivity::class.java)
    }
}
