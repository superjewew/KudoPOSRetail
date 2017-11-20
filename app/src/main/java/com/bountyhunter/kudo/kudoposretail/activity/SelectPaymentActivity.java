package com.bountyhunter.kudo.kudoposretail.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bountyhunter.kudo.kudoposretail.MposPrinter;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.Receipt;
import com.bountyhunter.kudo.kudoposretail.api.LoginResponse;
import com.bountyhunter.kudo.kudoposretail.api.Transaction;
import com.bountyhunter.kudo.kudoposretail.api.TransactionRequest;
import com.bountyhunter.kudo.kudoposretail.paymentmethod.CardPaymentMethod;
import com.bountyhunter.kudo.kudoposretail.rxjava.TransactionManager;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wangpos.sdk4.libbasebinder.BankCard;

@EActivity(R.layout.activity_select_payment)
public class SelectPaymentActivity extends AppCompatActivity {

    MposPrinter printer;
    final Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupHomeButton();

        HashMap<String, Integer> products = new HashMap<>();
        products.put("Minyak", 52000);
        products.put("Mie", 12000);
        setupPrinter(new Receipt(products,2));
    }

    private void setupHomeButton() {
        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupPrinter(Receipt receipt) {
        printer = MposPrinter.getInstance(this, receipt);
        printer.setupPrinter();
    }

    @Click(R.id.e_wallet_button)
    public void goToEWalletPayment() {
        EwalletMethodActivity_.IntentBuilder_ builder = EwalletMethodActivity_.intent(this);
        builder.start();
    }

    @Click(R.id.card_button)
    public void goToCardPayment() {
        CardMethodActivity_.IntentBuilder_ builder = CardMethodActivity_.intent(this);
        builder.start();
    }

    @Click(R.id.cash_button)
    public void goToCashPayment() {
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.submitTransaction(new TransactionRequest());

        Subscriber<LoginResponse> subscriber = new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {
                Log.d(this.getClass().getSimpleName(), "Complete submit transaction");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                Toast.makeText(mContext,loginResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }
        };

        transactionManager.submitTransaction(new TransactionRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        printer.print();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
