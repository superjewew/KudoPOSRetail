package com.bountyhunter.kudo.kudoposretail.receipt;

import com.bountyhunter.kudo.kudoposretail.Card;
import com.bountyhunter.kudo.kudoposretail.ReceiptString;
import com.bountyhunter.kudo.kudoposretail.util.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wangpos.sdk4.libbasebinder.Printer;

/**
 * Created by norman on 11/23/17.
 */

public class SettlementReceipt extends BaseReceipt {

    public static final String E_MONEY_SALE_KEY = "E_MONEY";
    public static final String E_MONEY_VOID_KEY = "E_MONEY_VOID";
    public static final String CARD_SALE_KEY = "CARD";
    public static final String CARD_VOID_KEY = "CARD_VOID";
    public static final String CASH_SALE_KEY = "CASH";
    public static final String CASH_VOID_KEY = "CASH_VOID";

    public SettlementReceipt(HashMap<String, Integer> settlements) {
        super(null, settlements, SETTLEMENT, null);
        generateContents();
    }

    @Override
    void generateHeader() {
        List<ReceiptString> header = new ArrayList<>();

        header.add(new ReceiptString("Settlement Receipt", 30, Printer.Align.CENTER, true, false));
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

    }

    @Override
    protected void generateItems(HashMap<String, Integer> settlements) {
        List<ReceiptString> contents = new ArrayList<>();

        contents.add(new ReceiptString("E-Money", 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Sale: " + settlements.get(E_MONEY_SALE_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Void: " + settlements.get(E_MONEY_VOID_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString(" ", 24, Printer.Align.LEFT, false, false));

        contents.add(new ReceiptString("Kartu", 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Sale: " + settlements.get(CARD_SALE_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Void: " + settlements.get(CARD_VOID_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString(" ", 24, Printer.Align.LEFT, false, false));

        contents.add(new ReceiptString("Tunai", 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Sale: " + settlements.get(CASH_SALE_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString("Void: " + settlements.get(CASH_VOID_KEY), 24, Printer.Align.LEFT, false, false));
        contents.add(new ReceiptString(" ", 24, Printer.Align.LEFT, false, false));

        mContents.addAll(contents);
    }

    @Override
    protected void generateTotalPrice(HashMap<String, Integer> settlements) {
        int total;

        total = settlements.get(E_MONEY_SALE_KEY) + settlements.get(CARD_SALE_KEY) + settlements.get(CASH_SALE_KEY);
        total -= (settlements.get(E_MONEY_VOID_KEY) + settlements.get(CARD_VOID_KEY) +settlements.get(CASH_VOID_KEY));

        mContents.add(new ReceiptString("Total: " + NumberUtils.formatPrice(total), 24, Printer.Align.LEFT, false, false));
        mContents.add(new ReceiptString(" ", 30, Printer.Align.LEFT, false, false));
    }
}
