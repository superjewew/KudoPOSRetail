package com.bountyhunter.kudo.kudoposretail;

import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import wangpos.sdk4.libbasebinder.BankCard;

/**
 * Created by norman on 11/17/17.
 */

public class CardPaymentMethod implements PaymentMethod {

    public final String TAG = getClass().getSimpleName();
    private BankCard mBankCard;
    private Thread mThread;
    private boolean mCanRead;
    private boolean mMagFlag;

    public CardPaymentMethod(BankCard bankCard) {
        mBankCard = bankCard;
    }

    @Override
    public void listen() {
        mCanRead = true;
        mMagFlag = true;
        createThreadForListening();
    }

    private void createThreadForListening() {
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("HANDLER", "MAG Test Started");
                    startListening();
                }
            });
            mThread.start();
        }
    }

    private void startListening() {
        Log.v(TAG, "bankcard readcard");
        byte[] respdata = new byte[1024];
        int[] resplen = new int[1];
        int retvalueocmag = -1;
        int retvaluedetemag = -1;
        boolean magflag = false;
        do {
            try {
                retvalueocmag = this.mBankCard.openCloseCardReader(4, 1);
            } catch (RemoteException e2) {
                e2.printStackTrace();
            }
            Log.v(TAG, "SDK_OpenCloseCardReader:" + retvalueocmag);
            if (!this.mCanRead || retvalueocmag == 0) {
                do {
                    try {
                        retvaluedetemag = this.mBankCard.cardReaderDetact(0, 4, 5, respdata, resplen, "app1");
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    Log.v(TAG, "SDK_CardReaderDetact:" + retvaluedetemag);
                    if (retvaluedetemag == 2 || retvaluedetemag == 0) {
                        magflag = true;
                    }
                    if (!this.mCanRead || magflag) {
                        if (retvaluedetemag != 0) {
//                            this.mText = "MAG Test success";
                        } else if (retvaluedetemag == 2) {
//                            this.mText = "MAG Test error";
                        } else {
//                            this.mText = "MAG Test fail";
                        }
                        mMagFlag = false;
                    }
                } while (this.mMagFlag);

//                Message msg = mHandler.obtainMessage(3);
//                if (retvaluedetemag != 0) {
//                    msg.arg1 = 1;
//                } else {
//                    msg.arg1 = 0;
//                }
//                mHandler.sendMessage(msg);
            }
        } while (this.mMagFlag);
    }

    public void stopListening() {
        try {
            mCanRead = false;
            if (mThread != null) {
                mThread.interrupt();
                mThread = null;
            }
            this.mBankCard.breakOffCommand();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
