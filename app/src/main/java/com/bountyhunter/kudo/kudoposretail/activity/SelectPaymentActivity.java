package com.bountyhunter.kudo.kudoposretail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bountyhunter.kudo.kudoposretail.MposPrinter;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.api.Transaction;
import com.bountyhunter.kudo.kudoposretail.model.CartItem;
import com.bountyhunter.kudo.kudoposretail.receipt.Receipt;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.METHOD_CASH;

@EActivity(R.layout.activity_select_payment)
public class SelectPaymentActivity extends AppCompatActivity {

    public static final int EWALLET_PROCESS_REQUEST = 0;

    public static final int CARD_PROCESS_REQUEST = 1;

    public static final int CASH_PROCESS_REQUEST = 2;

    MposPrinter printer;
    final Context mContext = this;

    private Realm mRealm;

    private ArrayList<Transaction> transactions = new ArrayList<>();

    private Receipt mReceipt;

    private HashMap<String, Integer> products = new HashMap<>();

    private long mTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupHomeButton();

        mRealm = Realm.getDefaultInstance();

        RealmResults<CartItem> results = mRealm.where(CartItem.class).findAll();

        for (CartItem item : results) {
            mTotalPrice += item.getMItemQuantity() * item.getMItemQuantity();
            products.put(item.getMItemName(), (int) (item.getMItemPrice() * item.getMItemQuantity()));
            Transaction transaction = new Transaction(item.getMItemId(), item.getMItemQuantity());
            transactions.add(transaction);
        }

        setupPrinter(null);
    }

    private void setupHomeButton() {
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
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
        builder.startForResult(CARD_PROCESS_REQUEST);
    }

    @Click(R.id.cash_button)
    public void goToCashPayment() {
        Intent intent = ProcessTransactionActivity.Companion.newIntent(this, null, METHOD_CASH);
        startActivityForResult(intent, CASH_PROCESS_REQUEST);
    }

    @OnActivityResult(CASH_PROCESS_REQUEST)
    void onResult(int resultCode, Intent data) {
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
