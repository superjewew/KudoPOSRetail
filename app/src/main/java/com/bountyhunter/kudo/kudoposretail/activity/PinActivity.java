package com.bountyhunter.kudo.kudoposretail.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.MposPrinter;
import com.bountyhunter.kudo.kudoposretail.R;
import com.bountyhunter.kudo.kudoposretail.api.LoginResponse;
import com.bountyhunter.kudo.kudoposretail.api.Transaction;
import com.bountyhunter.kudo.kudoposretail.api.TransactionRequest;
import com.bountyhunter.kudo.kudoposretail.model.CartItem;
import com.bountyhunter.kudo.kudoposretail.receipt.Receipt;
import com.bountyhunter.kudo.kudoposretail.Wangpos;
import com.bountyhunter.kudo.kudoposretail.rxjava.TransactionManager;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.parceler.Parcels;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import wangpos.sdk4.base.ICallbackListener;
import wangpos.sdk4.libbasebinder.Core;

import static com.bountyhunter.kudo.kudoposretail.receipt.Receipt.METHOD_CARD;
import static com.bountyhunter.kudo.kudoposretail.util.TransactionUtil.generateTransNo;

@EActivity(R.layout.activity_pin)
public class PinActivity extends AppCompatActivity {

    public static final int PROCESS_REQUEST = 0;

    public static final String EXTRA_CARD = "card";
    public static final String TAG = "PIN_ACTIVITY";
    private Context mContext;
    private Core mCore;
    private boolean mAllowSendRotationCommand;
    private EventHandler mHandler = null;
    private MposPrinter mPrinter;
    private Thread mThread;

    @ViewById(R.id.button0)
    Button btnb0;
    @ViewById(R.id.button1)
    Button btnb1;
    @ViewById(R.id.button2)
    Button btnb2;
    @ViewById(R.id.button3)
    Button btnb3;
    @ViewById(R.id.button4)
    Button btnb4;
    @ViewById(R.id.button5)
    Button btnb5;
    @ViewById(R.id.button6)
    Button btnb6;
    @ViewById(R.id.button7)
    Button btnb7;
    @ViewById(R.id.button8)
    Button btnb8;
    @ViewById(R.id.button9)
    Button btnb9;
    @ViewById(R.id.buttonclean)
    Button btnclean;
    @ViewById(R.id.buttonconfirm)
    Button btnconfirm;
    @ViewById(R.id.buttoncan)
    Button btncancel;

    @Extra
    Parcelable mCard;
    private Realm mRealm;
    private String transNo;
    private int mTotalPrice;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private HashMap<String, Integer> products = new HashMap<>();
    private Card mCardInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Wangpos wangpos = Wangpos.getInstance(this);
        mContext = this;
        mHandler = new EventHandler();
        mRealm = Realm.getDefaultInstance();

        RealmResults<CartItem> results = mRealm.where(CartItem.class).findAll();
//
        for(CartItem item : results) {
            mTotalPrice += item.getMItemQuantity() * item.getMItemQuantity();
            products.put(item.getMItemName(), (int) (item.getMItemPrice() * item.getMItemQuantity()));
            Transaction transaction = new Transaction(item.getMItemId(), item.getMItemQuantity());
            transactions.add(transaction);
        }

        Subscriber<Core> subscriber = new Subscriber<Core>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Core core) {
                mCore = core;
                mThread = new PINThread();
                mThread.start();
            }
        };

        wangpos.getObservableBaseCore()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mThread != null && !mThread.isInterrupted()) {
            mThread.interrupt();
            mThread = null;
        }
    }

    @AfterExtras
    void setupDataToBePrinted() {
        mCardInstance = Parcels.unwrap(mCard);
    }

    private ICallbackListener callback = new ICallbackListener.Stub() {
        public int emvCoreCallback(int command, byte[] data, byte[] result, int[] resultlen) throws RemoteException {
            Log.e("zys", "callback ****************");
            if (command != 2818) {
                return -1;
            }
            byte b = data[0];
            Message msg;
            Bundle bd;
            if (b == (byte) 1) {
                mAllowSendRotationCommand = true;
                Log.e("PINPad", "pin pad init data len is " + data.length);
                msg = new Message();
                msg.what = 1;
                bd = new Bundle();
                bd.putByteArray("data", data);
                msg.setData(bd);
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
                try {
                    mCore.generatePINPrepareData(result, btnb1, btnb2, btnb3, btnb4, btnb5, btnb6, btnb7, btnb8, btnb9, btnb0, btncancel, btnconfirm, btnclean, (Activity) mContext);
                    resultlen[0] = 105;
                } catch (Exception e) {
                    Log.e("PINPad", "mReceiver RemoteException " + e.toString());
                }
            } else {
                b = data[0];
                if (b == (byte) 2) {
                    result[0] = (byte) 0;
                    resultlen[0] = 1;
                    msg = new Message();
                    msg.what = 2;
                    bd = new Bundle();
                    bd.putByteArray("data", data);
                    msg.setData(bd);
                    if (mHandler != null) {
                        mHandler.sendMessage(msg);
                    }
                } else {
                    b = data[0];
                    if (b == (byte) 3) {
                        mAllowSendRotationCommand = false;
                        result[0] = (byte) 0;
                        resultlen[0] = 1;
                        msg = new Message();
                        msg.what = 3;
                        bd = new Bundle();
                        bd.putByteArray("data", data);
                        msg.setData(bd);
                        if (mHandler != null) {
                            mHandler.sendMessage(msg);
                        }
                    }
                }
            }
            return 0;
        }
    };

    class EventHandler extends Handler {
        EventHandler() {
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            byte[] data;
            switch (msg.what) {
                case 1:
                    data = msg.getData().getByteArray("data");
                    btnb1.setText("" + (data[4] - 48));
                    btnb2.setText("" + (data[5] - 48));
                    btnb3.setText("" + (data[6] - 48));
                    btnb4.setText("" + (data[7] - 48));
                    btnb5.setText("" + (data[8] - 48));
                    btnb6.setText("" + (data[9] - 48));
                    btnb7.setText("" + (data[10] - 48));
                    btnb8.setText("" + (data[11] - 48));
                    btnb9.setText("" + (data[12] - 48));
                    btnb0.setText("" + (data[13] - 48));
                    return;
                case 2:
                    if (msg.getData() != null) {
                        int count = msg.getData().getByteArray("data")[1];
                        String stars = "";
                        for (int i = 0; i < count; i++) {
                            stars = stars + "*";
                        }
                        ((TextView) findViewById(R.id.textView)).setText(stars);
                    }
                    return;
                case 3:
                    RestoreKeyPad();
                    data = msg.getData().getByteArray("data");
                    byte b = data[1];
                    if (b == (byte) 0) {
                        b = data[2];
                        if (b == (byte) 0) {
                            ((TextView) findViewById(R.id.textView)).setText("No PIN inputed");
                            return;
                        }
                        b = data[2];
                        int pinlen;
                        byte[] PINData;
                        //TODO Move to success payment after PIN Input
                        if (b == (byte) 1) {
                            pinlen = data[3];
                            Log.e("PINPad", "Pain pinlen is " + pinlen);
                            PINData = new byte[(pinlen + 1)];
                            PINData[0] = data[1];
                            System.arraycopy(data, 4, PINData, 1, pinlen);
                            try {
                                Log.d(TAG, new String(PINData, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
//                            printReceipt();
                            goToProcessTransaction();
                            return;
                        }
                        b = data[2];
                        if (b == (byte) 2) {
                            pinlen = data[3];
                            Log.e("PINPad", "Encrypt pinlen is " + pinlen);
                            PINData = new byte[(pinlen + 1)];
                            PINData[0] = data[1];
                            System.arraycopy(data, 4, PINData, 1, pinlen);
                            try {
                                Log.d(TAG, new String(PINData, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
//                            printReceipt();
                            goToProcessTransaction();
                            return;
                        }
                        return;
                    }
                    b = data[1];
                    if (b == (byte) -4) {
                        ((TextView) findViewById(R.id.textView)).setText("User canceled");
                        return;
                    }
                    b = data[1];
                    if (b == (byte) -11) {
                        ((TextView) findViewById(R.id.textView)).setText("bypass");
                        return;
                    }
                    b = data[1];
                    if (b == (byte) -10) {
                        ((TextView) findViewById(R.id.textView)).setText("Error");
                        return;
                    }
                    b = data[1];
                    if (b == (byte) -5) {
                        ((TextView) findViewById(R.id.textView)).setText("Timeout");
                        return;
                    }
                    b = data[1];
                    if (b == (byte) -14) {
                        ((TextView) findViewById(R.id.textView)).setText("No PAN");
                        return;
                    } else {
                        ((TextView) findViewById(R.id.textView)).setText("Other Error");
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public class PINThread extends Thread {
        public void run() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

            }
            String pan = "1111111111111111";
            int ret = -1;
            try {
                ret = mCore.startPinInput(60, "app1", 1, 4, 12, 1, new byte[8], pan.length(), pan.getBytes("UTF-8"), callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (ret == 0) {
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("zys", "rotation1111----------mAllowSendRotationCommand = " + this.mAllowSendRotationCommand);
        if (this.mAllowSendRotationCommand) {
            Log.e("zys", "rotation----------");
            try {
                Log.e("zys", "1+++++++++++");
                Log.e("zys", "2===========");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void goToProcessTransaction() {
        Intent intent = ProcessTransactionActivity.Companion.newIntent(this, mCardInstance, METHOD_CARD);
        startActivityForResult(intent, PROCESS_REQUEST);
    }

    @OnActivityResult(PROCESS_REQUEST)
    void onResult(int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            this.finish();
        }
    }

    private void RestoreKeyPad() {
        this.btnb1.setText("1");
        this.btnb2.setText("2");
        this.btnb3.setText("3");
        this.btnb4.setText("4");
        this.btnb5.setText("5");
        this.btnb6.setText("6");
        this.btnb7.setText("7");
        this.btnb8.setText("8");
        this.btnb9.setText("9");
        this.btnb0.setText("0");
    }
}
