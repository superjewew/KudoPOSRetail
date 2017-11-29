package com.bountyhunter.kudo.kudoposretail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bountyhunter.kudo.kudoposretail.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import static com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.METHOD_CASH;

@EActivity(R.layout.activity_select_payment)
public class SelectPaymentActivity extends AppCompatActivity {

    public static final int EWALLET_PROCESS_REQUEST = 0;

    public static final int CARD_PROCESS_REQUEST = 1;

    public static final int CASH_PROCESS_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupHomeButton();
    }

    private void setupHomeButton() {
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Click(R.id.e_wallet_button)
    public void goToEWalletPayment() {
        EwalletMethodActivity_.IntentBuilder_ builder = EwalletMethodActivity_.intent(this);
        builder.startForResult(EWALLET_PROCESS_REQUEST);
    }

    @Click(R.id.card_button)
    public void goToCardPayment() {
        CardMethodActivity_.IntentBuilder_ builder = CardMethodActivity_.intent(this);
        builder.startForResult(CARD_PROCESS_REQUEST);
    }

    @Click(R.id.cash_button)
    public void goToCashPayment() {
        Intent intent = ProcessTransactionActivity.Companion.newIntent(this, null, METHOD_CASH);
        startActivityForResult(intent, CASH_PROCESS_REQUEST);
    }

    @OnActivityResult(CASH_PROCESS_REQUEST)
    void onCashResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
    }

    @OnActivityResult(CARD_PROCESS_REQUEST)
    void onCardResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
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
