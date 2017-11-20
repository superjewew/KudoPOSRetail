package com.bountyhunter.kudo.kudoposretail.paymentmethod;

import android.os.RemoteException;
import android.util.Log;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.event.CardDetectedSuccessEvent;
import com.bountyhunter.kudo.kudoposretail.event.CardPaymentSuccessEvent;

import org.greenrobot.eventbus.EventBus;

import rx.Observable;
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
                retvalueocmag = this.mBankCard.openCloseCardReader(4 | 1, 1);
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
                        if (retvaluedetemag == 0) {
                            handleResponse(respdata, resplen[0]);
//                            EventBus.getDefault().post(new CardDetectedSuccessEvent(new Card("","")));
                        } else if (retvaluedetemag == 2) {
//                            this.mText = "MAG Test error";
                        } else {
//                            this.mText = "MAG Test fail";
                        }
                        mMagFlag = false;
                    }
                } while (this.mMagFlag);

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
            if(mBankCard != null) {
                mBankCard.breakOffCommand();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleResponse(byte[] response, int resplen) {
        byte[] data1 = new byte[77];
        int[] len1 = new int[1];
        byte[] data2 = new byte[38];
        int[] len2 = new int[1];
        byte[] data3 = new byte[115];
        int[] len3 = new int[1];

        byte[] dataATR = new byte[resplen];

        int returnCode;

        returnCode = mBankCard.parseMagnetic(response, resplen, data1, len1, data2, len2, data3, len3);
        if(isMethodCallSuccess(returnCode)) {
            Log.d(TAG, "MAG card swiped");
            Log.d(TAG, new String(data1));
            Log.d(TAG, new String(data2));
            Log.d(TAG, new String(data3));

            EventBus.getDefault().post(new CardDetectedSuccessEvent(setupCard(data1)));
        }
    }

    private Card setupCard(byte[] data1) {
        String[] cardParsedData = getPanAndHolderString(new String(data1));
        return new Card(cardParsedData[0],cardParsedData[1]);
    }

    private String[] getPanAndHolderString(String magneticStripeData) {
        String[] results = magneticStripeData.split("\\^");
        if(results[1].length() != 0) {
            results[1] = results[1].substring(0, 27);
        }
        return results;
    }

    private boolean isMethodCallSuccess(int returnCode) {
        return returnCode == 0;
    }
}
