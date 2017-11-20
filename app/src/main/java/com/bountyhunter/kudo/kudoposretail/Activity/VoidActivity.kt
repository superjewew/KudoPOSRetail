package com.bountyhunter.kudo.kudoposretail.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.rxjava.VoidManager
import kotlinx.android.synthetic.main.activity_void.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class VoidActivity : AppCompatActivity() {

    private val voidManager by lazy { VoidManager() }

    private var error: Boolean = false

    private var focusView: View? = null

    private val compositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_void)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        void_button.setOnClickListener { attemptVoid() }
    }

    override fun onPause() {
        super.onPause()
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.clear()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                this.finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun attemptVoid() {
        void_button.isEnabled = false

        resetErrorAndFlags()

        val transNo = getTransNumberFromEditText()
        val pin = getPinFromEditText()

        checkPasswordValid(transNo, pin)

        checkTransNumberValid(transNo)

        checkIfError()

        val disposable = voidManager.void(transNo, Integer.valueOf(pin))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data -> showToast(data.message)
                        },
                        {
                            error -> showToast("Gagal membatalkan transaksi")
                        },
                        {
                            void_button.isEnabled = true
                        }
                )
        compositeSubscription.add(disposable)
    }

    private fun resetErrorAndFlags() {
        transaction_number_et.error = null
        pin_et.error = null

        error = false
        focusView = null
    }

    private fun getTransNumberFromEditText() : String {
        return transaction_number_et.text.toString()
    }

    private fun getPinFromEditText() : String {
        return pin_et.text.toString()
    }

    private fun checkPasswordValid(transNo: String, pin : String) {
        if (!TextUtils.isEmpty(transNo) && !isPinValid(pin)) {
            pin_et.error = getString(R.string.error_invalid_password)
            focusView = pin_et
            error = true
        }
    }

    private fun checkTransNumberValid(transNo: String) {
        if (TextUtils.isEmpty(transNo)) {
            transaction_number_et.error = getString(R.string.error_field_required)
            focusView = transaction_number_et
            error = true
        } else if (!isTransactionValid(transNo)) {
            transaction_number_et.error = getString(R.string.error_invalid_email)
            focusView = transaction_number_et
            error = true
        }
    }

    private fun checkIfError() {
        if (error) {
            focusView?.requestFocus()
        } else {
//            showProgress(true)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    private fun isTransactionValid(transNo: String): Boolean {
        return transNo.length > 6
    }

    private fun isPinValid(password: String): Boolean {
        return password.length > 4
    }

    companion object {
        fun newIntent(context: Context) : Intent {
            val intent = Intent(context, VoidActivity::class.java)
            return intent
        }
    }
}
