package com.bountyhunter.kudo.kudoposretail;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;
import wangpos.sdk4.libbasebinder.Printer.Align;
import wangpos.sdk4.libbasebinder.Printer.Font;

/**
 * Created by norman on 11/17/17.
 */

public class MposPrinter {

    private static MposPrinter sInstance;

    private Printer mPrinter;
    private System mSystem;
    private Context mContext;
    private List<ReceiptString> mStringsToBePrinted;
    private boolean mLoop = false;
    private boolean mThreadRunning = false;
    private boolean mDoneInit = false;

    public MposPrinter(Context context, List<ReceiptString> stringsToBePrinted) {
        mContext = context;
        mStringsToBePrinted = stringsToBePrinted;
    }

    public void setupPrinter() {
        new Thread() {
            public void run() {
                mPrinter = new Printer(mContext);
                mDoneInit = true;
            }
        }.start();
    }

    public void print() {
        mLoop = true;
        if(mDoneInit && !mThreadRunning) {
            new PrintThread().start();
        }
    }

    private int printString(ReceiptString toBePrinted) {
        return printString(toBePrinted.getContent(), toBePrinted.getFontSize(),
                toBePrinted.getAlignment(), toBePrinted.isBold(), toBePrinted.isItalic());
    }

    private int printString(String toBePrinted) {
        return printString(toBePrinted, 25, Align.LEFT, false, false);
    }

    private int printString(String toBePrinted, int fontSize, Align alignment, boolean bold, boolean italic) {
        int result = 1;
        try {
            result = this.mPrinter.printString(toBePrinted, fontSize, alignment, bold, italic);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void testPrintString(int result) {
        try {
            result = this.mPrinter.printString("DaZhongDianPing", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("www.dianping.com", 25, Align.CENTER, true, false);
            result = this.mPrinter.printString("  ", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("--------------------------------------------", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("  ", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("Payment Succeed ", 30, Align.CENTER, true, false);
            result = this.mPrinter.printString("  ", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("Meal Package:KFC $100 coupons", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("Selling Price:$90", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("Merchant Name:KFC（ZS Park）", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("Payment Time:17/3/29 9:27", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("  ", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("--------------------------------------------", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("  ", 30, Align.CENTER, false, false);
            result = this.mPrinter.printString("NO. of Coupons:5", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("Total Amount:$450", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("SN:1234 4567 4565", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("       1234 4567 4565", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("       1234 4567 4565", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("       1234 4567 4565", 25, Align.LEFT, false, false);
            result = this.mPrinter.printString("       1234 4567 4565", 25, Align.LEFT, false, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void testPrintStringExt(int result) {
        try {
            result = this.mPrinter.printStringExt("Default Font ", 0, 0.0f, 2.0f, Font.DEFAULT, 30, Align.LEFT, false, false, false);
            result = this.mPrinter.printStringExt("Default Bold Font ", 0, 0.0f, 2.0f, Font.DEFAULT_BOLD, 30, Align.LEFT, false, false, false);
            result = this.mPrinter.printStringExt("Monospace Font ", 0, 0.0f, 2.0f, Font.MONOSPACE, 30, Align.LEFT, false, false, false);
            result = this.mPrinter.printStringExt("Sans Serif Font ", 0, 0.0f, 2.0f, Font.SANS_SERIF, 30, Align.LEFT, false, false, false);
            result = this.mPrinter.printStringExt("Serif Font ", 0, 0.0f, 2.0f, Font.SERIF, 30, Align.LEFT, false, false, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public class PrintThread extends Thread {
        public void run() {
            mThreadRunning = true;
            int result = 0;
            do {
                try {
                    result = mPrinter.printInit();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Log.v("zhangning", "print data init " + result);
                try {
                    for(ReceiptString toBePrinted : mStringsToBePrinted) {
                        result = printString(toBePrinted);
                    }
                    if (mPrinter.printPaper(100) != 0) {
                        mLoop = false;
                    }
                    result = mPrinter.printFinish();
                    if (result != 0) {
                        mLoop = false;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } while (mLoop);
            mThreadRunning = false;
        }
    }
}
