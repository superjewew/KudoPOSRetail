package com.bountyhunter.kudo.kudoposretail;

import android.content.Context;

import rx.Observable;
import rx.Subscriber;
import sdk4.wangpos.libemvbinder.EmvCore;
import wangpos.sdk4.libbasebinder.BankCard;
import wangpos.sdk4.libbasebinder.Core;
import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/17/17.
 */

public class Wangpos {

    private static Wangpos sInstance;
    private Context mContext;
    private Core mBaseCore;
    private BankCard mBankCard;
    private EmvCore mEmvCore;
    private boolean mDoneInit;

    private Wangpos(Context context) {
        mContext = context;
    }

    public static Wangpos getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Wangpos(context.getApplicationContext());
        }

        return sInstance;
    }

    public Observable<BankCard> getObservableBankCard() {
        return Observable.create(new Observable.OnSubscribe<BankCard>() {
            @Override
            public void call(Subscriber<? super BankCard> subscriber) {
                setupBankCard(mContext);
                subscriber.onNext(getBankCard());
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Core> getObservableBaseCore() {
        return Observable.create(new Observable.OnSubscribe<Core>() {
            @Override
            public void call(Subscriber<? super Core> subscriber) {
                setupBaseCore(mContext);
                subscriber.onNext(getBaseCore());
                subscriber.onCompleted();
            }
        });
    }

    private void setupBaseCore(Context context) {
        mBaseCore = new Core(context);
    }

    private void setupEmvCore(Context context) {
        mEmvCore = new EmvCore(context);
    }

    private void setupBankCard(Context context) {
        mBankCard = new BankCard(context);
    }

    public BankCard getBankCard() {
        return mBankCard;
    }

    public Core getBaseCore() {
        return mBaseCore;
    }
}
