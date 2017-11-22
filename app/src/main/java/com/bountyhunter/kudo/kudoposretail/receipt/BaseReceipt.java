package com.bountyhunter.kudo.kudoposretail.receipt;

import android.util.Log;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.ReceiptString;
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/22/17.
 */

public abstract class BaseReceipt {
    public static final int METHOD_EWALLET = 0;
    public static final int METHOD_CARD = 1;
    public static final int METHOD_CASH = 2;

    List<ReceiptString> mContents = new ArrayList<>();

    private Card mCard = new Card("", "");

    public BaseReceipt(HashMap<String, Integer> products, int method, Card card) {
        setCard(card);
        generateHeader();
        generatePaymentMethod(method);
        generateTimeAndDate();

        generateItems(products);
        generatePurchaseType();

        generateTotalPrice(products);

        generateFooter();

        for (ReceiptString string: mContents) {
            Log.d("RECEIPT", string.getContent());
        }
    }

    abstract void generatePurchaseType();

    public void setCard(Card card) {
        if(card != null) {
            mCard = card;
        }
    }

    abstract void generateHeader();

    private void generateTimeAndDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        mContents.add(new ReceiptString(dateFormat.format(cal.getTime()), 24, Printer.Align.LEFT, false, false));
        mContents.add(new ReceiptString(timeFormat.format(cal.getTime()), 24, Printer.Align.LEFT, false, false));
        mContents.add(new ReceiptString("Trace No XXXXXX", 24, Printer.Align.LEFT, false, false));
    }

    private void generateItems(HashMap<String, Integer> productMap) {
        List<ReceiptString> products = new ArrayList<>();

        for(String productName : productMap.keySet()) {
            products.add(new ReceiptString(productName + " : " + NumberUtils.formatPrice(productMap.get(productName)), 24, Printer.Align.LEFT, false, false));
        }
        products.add(new ReceiptString(" ", 30, Printer.Align.LEFT, false, false));

        mContents.addAll(products);
    }

    private void generateTotalPrice(HashMap<String, Integer> productMap) {
        int totalPrice = calculateTotal(productMap);
        mContents.add(new ReceiptString("Total: " + NumberUtils.formatPrice(totalPrice), 24, Printer.Align.LEFT, false, false));
        mContents.add(new ReceiptString(" ", 30, Printer.Align.LEFT, false, false));
    }

    private int calculateTotal(HashMap<String, Integer> productMap) {
        int total = 0;
        for(String productName : productMap.keySet()) {
            total += productMap.get(productName);
        }
        return total;
    }

    private void generatePaymentMethod(int method) {
        switch(method) {
            case METHOD_EWALLET:
                mContents.add(new ReceiptString("E-Wallet", 24, Printer.Align.LEFT, false, false));
                break;
            case METHOD_CARD:
                List<ReceiptString> footer = new ArrayList<>();

                footer.add(new ReceiptString("Card No " + mCard.getCardNumber(), 24, Printer.Align.LEFT, false, false));
                footer.add(new ReceiptString("Card Issuer " + mCard.getCardIssuer() , 24, Printer.Align.LEFT, false, false));

                mContents.addAll(footer);
                break;
            case METHOD_CASH:
                mContents.add(new ReceiptString("CASH", 24, Printer.Align.LEFT, false, false));
                break;
        }
    }

    private void generateFooter() {
        mContents.add(new ReceiptString(" ", 30, Printer.Align.LEFT, false, false));
        mContents.add(new ReceiptString("APPROVED", 24, Printer.Align.LEFT, true, false));
        mContents.add(new ReceiptString("Thank You", 24, Printer.Align.LEFT, true, false));
    }
}
