package com.bountyhunter.kudo.kudoposretail.receipt;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.ReceiptString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/18/17.
 */

public class Receipt extends BaseReceipt {
    public static final int METHOD_EWALLET = 0;
    public static final int METHOD_CARD = 1;
    public static final int METHOD_CASH = 2;

    public Receipt(HashMap<String, Integer> products, int method, Card card) {
        super(products, method, card);
    }

    public List<ReceiptString> getContents() {
        return mContents;
    }

    @Override
    void generateHeader() {
        List<ReceiptString> header = new ArrayList<>();

        header.add(new ReceiptString("Sale-Receipt", 30, Printer.Align.CENTER, true, false));
        header.add(new ReceiptString(" ", 30, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString("Kudo Store", 24, Printer.Align.CENTER, true, false));
        header.add(new ReceiptString("Kudoplex2", 24, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString("Radio Dalam", 24, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString("Jaksel", 24, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString(" ", 30, Printer.Align.CENTER, false, false));

        mContents.addAll(header);
    }

    @Override
    void generatePurchaseType() {
        mContents.add(new ReceiptString("Purchase", 24, Printer.Align.LEFT, false, false));
    }
}
