package com.bountyhunter.kudo.kudoposretail.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bountyhunter.kudo.kudoposretail.R
import com.bountyhunter.kudo.kudoposretail.rxjava.VoidManager
import kotlinx.android.synthetic.main.activity_void.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class VoidActivity : AppCompatActivity() {

    private val voidManager by lazy { VoidManager() }

    private var error: Boolean = false

    private var focusView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_void)

        void_button.setOnClickListener { attemptVoid() }
    }

    private fun attemptVoid() {
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
//                            data -> showToast(data.message)
                        },
                        {
//                            error -> showToast("GAGAL")
                        },
                        {}
                )

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

    private fun isTransactionValid(transNo: String): Boolean {
        return transNo.length > 6
    }

    private fun isPinValid(password: String): Boolean {
        return password.length > 4
    }
}
