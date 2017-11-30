package com.bountyhunter.kudo.kudoposretail.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import com.bountyhunter.kudo.kudoposretail.*
import com.bountyhunter.kudo.kudoposretail.api.LoginResponse
import com.bountyhunter.kudo.kudoposretail.api.Transaction
import com.bountyhunter.kudo.kudoposretail.api.TransactionRequest
import com.bountyhunter.kudo.kudoposretail.model.CartItem
import com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.*
import com.bountyhunter.kudo.kudoposretail.receipt.Receipt
import com.bountyhunter.kudo.kudoposretail.rxjava.TransactionManager
import com.bountyhunter.kudo.kudoposretail.util.TransactionUtil
import com.bountyhunter.kudo.kudoposretail.util.TransactionUtil.generateTransNo
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_process_transaction.*
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.view.animation.DecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation



class ProcessTransactionActivity : AppCompatActivity() {

    var mTransactions: ArrayList<Transaction> = ArrayList()

    private var mRealm: Realm? = Realm.getDefaultInstance()

    private var mTotalPrice: Double = 0.0

    private var mMethod: Int = 0

    private lateinit var mPrinter: MposPrinter

    private var mCard: Card = Card("","")

    private var mProducts = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_process_transaction)

        if(intent.getIntExtra("PAYMENT_METHOD", 0) == 2) {
            mMethod = METHOD_CASH
        }

        intent.getStringExtra("CARD_NUMBER")?.let {
            mMethod = METHOD_CARD
            mCard = Card(intent.getStringExtra("CARD_NUMBER"), intent.getStringExtra("CARD_ISSUER"))
        }

        home_button.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        val results = mRealm?.where(CartItem::class.java)?.findAll()

        if (results != null) {
            for (item in results) {
                mTotalPrice += item.mItemQuantity * item.mItemPrice
                mProducts.put(item.mItemName!!, (item.mItemPrice * item.mItemQuantity).toInt())
                val transaction = Transaction(item.mItemId!!, item.mItemQuantity)
                mTransactions.add(transaction)
            }
        }

        setupPrinter()

        submitTransactionToServer(mMethod, mTransactions, mTotalPrice)
    }

    private fun submitTransactionToServer(method: Int, transactions: ArrayList<Transaction>, totalPrice: Double) {
        val transNo = TransactionUtil.generateTransNo(method)
        val transactionManager = TransactionManager()
        val paymentMethod = generatePaymentMethodString(method)
        val receipt = Receipt(transNo, mProducts, method, mCard)

        val subscriber = object : Subscriber<LoginResponse>() {
            override fun onCompleted() {
                Log.d(this.javaClass.simpleName, "Complete submit transaction")
                saveSettlementToDatabase(transNo, totalPrice)
                mPrinter.setReceipt(receipt)
                mPrinter.print()
                home_button.visibility = View.VISIBLE
            }

            override fun onError(e: Throwable) {
                status_tv.text = "Transaksi Gagal"
            }

            override fun onNext(loginResponse: LoginResponse) {
                status_tv.text = "Transaksi Berhasil"
                transaction_iv.visibility = VISIBLE
                val fadeIn = AlphaAnimation(0f, 1f)
                fadeIn.interpolator = DecelerateInterpolator() //add this
                fadeIn.duration = 1000
                transaction_iv.animation = fadeIn
                clearCart()
            }
        }

        val request = TransactionRequest(1, paymentMethod, transNo, transactions)

        transactionManager.submitTransaction(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber)

    }

    private fun clearCart() {
        val cartDao = CartItemDao()
        cartDao.deleteAll()
    }

    private fun generatePaymentMethodString(method: Int): String = when (method) {
        METHOD_EWALLET -> "E-Wallet"
        METHOD_CARD -> "Kartu"
        else -> "Tunai"
    }

    private fun saveSettlementToDatabase(transNo: String, totalPrice: Double) {
        val settlementDAO = SettlementDAO()
        settlementDAO.create(transNo, totalPrice)
        settlementDAO.getAll()
                .map { it?.transId }
                .forEach { Log.i("PROCESS_TRANSACTION", it) }

    }

    private fun setupPrinter() {
        mPrinter = MposPrinter.getInstance(this, null)
    }

    companion object {
        fun newIntent(context: Context, card: Card?, method: Int): Intent {
            val intent = Intent(context, ProcessTransactionActivity::class.java)
            intent.putExtra("CARD_NUMBER", card?.cardNumber)
            intent.putExtra("CARD_ISSUER", card?.cardIssuer)
            intent.putExtra("PAYMENT_METHOD", method)
            return intent
        }
    }
}
