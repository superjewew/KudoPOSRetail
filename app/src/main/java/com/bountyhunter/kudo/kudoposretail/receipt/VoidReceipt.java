package com.bountyhunter.kudo.kudoposretail.receipt;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.ReceiptString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/22/17.
 */

public class VoidReceipt extends BaseReceipt {

    public VoidReceipt(String transNo, HashMap<String, Integer> products, int method, Card card) {
        super(transNo, products, method, card);
        generateContents();
    }

    @Override
    void generatePurchaseType() {
        mContents.add(new ReceiptString("Purchase VOID", 24, Printer.Align.LEFT, false, false));

    }

    @Override
    void generateHeader() {
        List<ReceiptString> header = new ArrayList<>();

        header.add(new ReceiptString("Void Receipt", 30, Printer.Align.CENTER, true, false));
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
    protected void generateItems(HashMap<String, Integer> productMap) {
        // Do not print items for void receipt
    }
}
