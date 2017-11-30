package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bountyhunter.kudo.kudoposretail.MposPrinter
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.SettlementDAO
import com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.*
import com.bountyhunter.kudo.kudoposretail.receipt.SettlementReceipt
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils
import com.bountyhunter.kudo.kudoposretail.util.TransactionUtil.determinePaymentMethodFromTransNo
import kotlinx.android.synthetic.main.activity_settlement.*

class SettlementActivity : AppCompatActivity() {

    var mEwalletAmount = 0.0

    var mCardAmount = 0.0

    var mCashAmount = 0.0

    var mEwalletVoidAmount = 0.0

    var mCardVoidAmount = 0.0

    var mCashVoidAmount = 0.0

    val mDao = SettlementDAO()

    private lateinit var mPrinter: MposPrinter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settlement)

        resetSettlement()
        calculateSettlement()

        print_settlement_button.setOnClickListener {
            mDao.deleteAll()
            resetSettlement()
            printReceipt()
        }

        setupPrinter()
    }

    private fun calculateSettlement() {
        for(settlement in mDao.getAll()) {
            val transNo = settlement.transId
            val method = determinePaymentMethodFromTransNo(transNo)
            when(method) {
                METHOD_EWALLET -> {
                    mEwalletAmount += settlement.price
                    if(settlement.status == "Void") mEwalletVoidAmount += settlement.price
                }
                METHOD_CARD -> {
                    mCardAmount += settlement.price
                    if(settlement.status == "Void") mCardVoidAmount += settlement.price
                }
                METHOD_CASH -> {
                    mCashAmount += settlement.price
                    if(settlement.status == "Void") mCashVoidAmount += settlement.price
                }
            }
            updateSettlement()
        }
    }

    private fun updateSettlement() {
        e_wallet_settlement_tv.text = NumberUtils.formatPrice(mEwalletAmount)
        card_settlement_tv.text = NumberUtils.formatPrice(mCardAmount)
        cash_settlement_tv.text = NumberUtils.formatPrice(mCashAmount)
    }

    private fun resetSettlement() {
        e_wallet_settlement_tv.text = NumberUtils.formatPrice(0.0)
        card_settlement_tv.text = NumberUtils.formatPrice(0.0)
        cash_settlement_tv.text = NumberUtils.formatPrice(0.0)
    }

    private fun printReceipt() {
        val settlements = HashMap<String, Int>()
        settlements.put(SettlementReceipt.E_MONEY_SALE_KEY, mEwalletAmount.toInt())
        settlements.put(SettlementReceipt.E_MONEY_VOID_KEY, mEwalletVoidAmount.toInt())
        settlements.put(SettlementReceipt.CARD_SALE_KEY, mCardAmount.toInt())
        settlements.put(SettlementReceipt.CARD_VOID_KEY, mCardVoidAmount.toInt())
        settlements.put(SettlementReceipt.CASH_SALE_KEY, mCashAmount.toInt())
        settlements.put(SettlementReceipt.CASH_VOID_KEY, mCashVoidAmount.toInt())

        val receipt = SettlementReceipt(settlements)
        mPrinter.setReceipt(receipt)
        mPrinter.print()
    }

    private fun setupPrinter() {
        mPrinter = MposPrinter.getInstance(this, null)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettlementActivity::class.java)
    }
}
