package com.bountyhunter.kudo.kudoposretail.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bountyhunter.kudo.kudoposretail.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Click(R.id.payment_method_button)
    public void goToPaymentMethod() {
        SelectPaymentActivity_.IntentBuilder_ builder = SelectPaymentActivity_.intent(this);
        builder.start();
    }

}
