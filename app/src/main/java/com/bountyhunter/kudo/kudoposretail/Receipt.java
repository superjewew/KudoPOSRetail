package com.bountyhunter.kudo.kudoposretail;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/18/17.
 */

public class Receipt {
    List<ReceiptString> mContents = new ArrayList<>();

    public Receipt(HashMap<String, Integer> products, int method) {
        generateHeader();
        generateTimeAndDate();
        generateItems(products);
        generateTotalPrice(products);
        generatePaymentMethod(method);

        for (ReceiptString string: mContents) {
            Log.d("RECEIPT", string.getContent());
        }
    }

    public List<ReceiptString> getContents() {
        return mContents;
    }

    private void generateHeader() {
        List<ReceiptString> header = new ArrayList<>();

        header.add(new ReceiptString("E-Receipt", 30, Printer.Align.CENTER, true, false));
        header.add(new ReceiptString("Kudo Store", 24, Printer.Align.LEFT, true, false));
        header.add(new ReceiptString("Kudoplex2", 24, Printer.Align.LEFT, false, false));
        header.add(new ReceiptString("Radio Dalam", 24, Printer.Align.LEFT, false, false));
        header.add(new ReceiptString("Jaksel", 24, Printer.Align.LEFT, false, false));
        header.add(new ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString(" ", 30, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString("Pembayaran Sukses", 30, Printer.Align.CENTER, false, false));
        header.add(new ReceiptString(" ", 30, Printer.Align.CENTER, false, false));

        mContents.addAll(header);
    }

    private void generateTimeAndDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

        mContents.add(new ReceiptString(format.format(cal.getTime()), 24, Printer.Align.LEFT, false, false));
    }

    private void generateItems(HashMap<String, Integer> productMap) {
        List<ReceiptString> products = new ArrayList<>();

        for(String productName : productMap.keySet()) {
            products.add(new ReceiptString(productName + " : " + productMap.get(productName), 24, Printer.Align.LEFT, false, false));
        }
        products.add(new ReceiptString(" ", 30, Printer.Align.LEFT, false, false));

        mContents.addAll(products);
    }

    private void generateTotalPrice(HashMap<String, Integer> productMap) {
        int totalPrice = calculateTotal(productMap);
        mContents.add(new ReceiptString("Total: " + totalPrice, 24, Printer.Align.LEFT, false, false));
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
            case 0:
                break;
            case 1:
                List<ReceiptString> footer = new ArrayList<>();

                footer.add(new ReceiptString("Approved", 24, Printer.Align.LEFT, true, false));
                footer.add(new ReceiptString("Card No 1234 XXXX XXXX XXXX", 24, Printer.Align.LEFT, false, false));
                footer.add(new ReceiptString("Card Type VISA CREDIT", 24, Printer.Align.LEFT, false, false));
                footer.add(new ReceiptString("Trace No XXXXXX", 24, Printer.Align.LEFT, false, false));

                mContents.addAll(footer);
                break;
            case 2:
                break;
        }
    }
}
