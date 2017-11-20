package com.bountyhunter.kudo.kudoposretail.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.bountyhunter.kudo.kudoposretail.MessageDialogFragment;
import com.bountyhunter.kudo.kudoposretail.MposPrinter;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.Receipt;
import com.bountyhunter.kudo.kudoposretail.Wangpos;
import com.bountyhunter.kudo.kudoposretail.event.CardDetectedSuccessEvent;
import com.bountyhunter.kudo.kudoposretail.event.CardPaymentSuccessEvent;
import com.bountyhunter.kudo.kudoposretail.paymentmethod.CardPaymentMethod;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.HashMap;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wangpos.sdk4.libbasebinder.BankCard;

@EActivity(R.layout.activity_card_method)
public class CardMethodActivity extends AppCompatActivity {

    private Wangpos mWangpos;
    private CardPaymentMethod mMethod;
    private MposPrinter mPrinter;
    private MessageDialogFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWangpos = Wangpos.getInstance(this);

        mDialog = MessageDialogFragment.newInstance(R.string.title_choose_another_payment_method,
                new MessageDialogFragment.DialogClickListener() {
            @Override
            public void onPositiveClicked() {
                finish();
            }

            @Override
            public void onNegativeClicked() {
                mDialog.dismiss();
            }
        });

        Subscriber<BankCard> subscriber = new Subscriber<BankCard>() {
            @Override
            public void onCompleted() {
                Log.d(this.getClass().getSimpleName(), "OnComplete Called");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(BankCard bankCard) {
                mMethod = new CardPaymentMethod(bankCard);
                mMethod.listen();
            }
        };

        mWangpos.getObservableBankCard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        mPrinter = MposPrinter.getInstance(this, new Receipt(generateDummyData(), 1));
        mPrinter.setupPrinter();
    }

    @Override
    public void onBackPressed() {
        mDialog.show(getFragmentManager(), "dialog");
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
        mMethod.stopListening();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCardDetectedSuccessEvent(CardDetectedSuccessEvent event) {
        PinActivity_.IntentBuilder_ builder = PinActivity_.intent(this)
                .extra(PinActivity.EXTRA_CARD, Parcels.wrap(event.getCard()));
        builder.start();
        this.finish();
    }

    private HashMap<String, Integer> generateDummyData() {
        HashMap<String, Integer> products = new HashMap<>();
        products.put("Minyak", 52000);
        products.put("Mie", 12000);

        return products;
    }
}
