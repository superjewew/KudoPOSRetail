package com.bountyhunter.kudo.kudoposretail.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bountyhunter.kudo.kudoposretail.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_select_payment)
public class SelectPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupHomeButton();
    }

    private void setupHomeButton() {
        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Click(R.id.e_wallet_button)
    public void goToEWalletPayment() {
        EwalletMethodActivity_.IntentBuilder_ builder = EwalletMethodActivity_.intent(this);
        builder.start();
    }

    @Click(R.id.card_button)
    public void goToCardPayment() {

    }

    @Click(R.id.cash_button)
    public void goToCashPayment() {
        CardMethodActivity_.IntentBuilder_ builder = CardMethodActivity_.intent(this);
        builder.start();
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
