package com.bountyhunter.kudo.kudoposretail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        Intent intent = new Intent(this, SelectPaymentActivity_.class);
        startActivity(intent);
    }

}
