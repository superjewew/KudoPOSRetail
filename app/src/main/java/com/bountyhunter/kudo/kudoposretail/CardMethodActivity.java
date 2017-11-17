package com.bountyhunter.kudo.kudoposretail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_card_method)
public class CardMethodActivity extends AppCompatActivity {

    private Wangpos mWangpos;

    private CardPaymentMethod mMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWangpos = Wangpos.getInstance(this);

        mMethod = new CardPaymentMethod(mWangpos.getBankCard());
    }
}
