package com.bountyhunter.kudo.kudoposretail;

import android.content.Context;

import sdk4.wangpos.libemvbinder.EmvCore;
import wangpos.sdk4.libbasebinder.BankCard;
import wangpos.sdk4.libbasebinder.Core;

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

    public void setupWangposCores() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setupBaseCore(mContext);
                setupEmvCore(mContext);
                setupBankCard(mContext);
                mDoneInit = true;
            }
        }).start();
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
