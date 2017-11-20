package com.bountyhunter.kudo.kudoposretail.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bountyhunter.kudo.kudoposretail.MposPrinter;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.Receipt;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

@EActivity(R.layout.activity_select_payment)
public class SelectPaymentActivity extends AppCompatActivity {

    MposPrinter printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupHomeButton();

        HashMap<String, Integer> products = new HashMap<>();
        products.put("Minyak", 52000);
        products.put("Mie", 12000);
        setupPrinter(new Receipt(products,2, null));
    }

    private void setupHomeButton() {
        ActionBar bar = getSupportActionBar();

        if(bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupPrinter(Receipt receipt) {
        printer = MposPrinter.getInstance(this, receipt);
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
