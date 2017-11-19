package com.bountyhunter.kudo.kudoposretail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/18/17.
 */

public class Receipt {
    List<ReceiptString> mContents = new ArrayList<>();

    public Receipt(int totalPrice, int method) {
        generateHeader();
        generateTimeAndDate();
        generateTotalPrice(totalPrice);
        generatePaymentMethod(method);
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

        mContents.addAll(header);
    }

    private void generateTimeAndDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

        mContents.add(new ReceiptString(format.format(cal.getTime()), 24, Printer.Align.LEFT, false, false));
    }

    private void generateTotalPrice(int price) {
        mContents.add(new ReceiptString("Total: " + price, 24, Printer.Align.LEFT, false, false));
    }

    private void generatePaymentMethod(int method) {
        switch(method) {
            case 0:
                break;
            case 1:
                List<ReceiptString> header = new ArrayList<>();

                header.add(new ReceiptString("Approved", 24, Printer.Align.LEFT, true, false));
                header.add(new ReceiptString("Card No 1234 XXXX XXXX XXXX", 24, Printer.Align.LEFT, false, false));
                header.add(new ReceiptString("Card Type VISA CREDIT", 24, Printer.Align.LEFT, false, false));
                header.add(new ReceiptString("Trace No XXXXXX", 24, Printer.Align.LEFT, false, false));

                mContents.addAll(header);
                break;
            case 2:
                break;
        }
    }
}
