package com.bountyhunter.kudo.kudoposretail.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bountyhunter.kudo.kudoposretail.event.CardPaymentSuccessEvent;
import com.bountyhunter.kudo.kudoposretail.paymentmethod.CardPaymentMethod;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.Wangpos;

import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardPaymentSuccessEvent(CardPaymentSuccessEvent event) {

    }
}
