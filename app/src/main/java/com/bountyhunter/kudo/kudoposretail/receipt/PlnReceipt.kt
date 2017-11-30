package com.bountyhunter.kudo.kudoposretail.receipt

import android.util.Log
import com.bountyhunter.kudo.kudoposretail.ReceiptString
import wangpos.sdk4.libbasebinder.Printer
import java.util.*

/**
 * Created by norman on 11/30/17.
 */

class PlnReceipt(transNo: String): BaseReceipt(null, null, 0, null) {

    override fun generateHeader() {
    }

    override fun generatePurchaseType() {
    }

    init {
        generateHeader(transNo)
        generateDateAndTime()
        generateCustomerDetail()
        generatePlnDetail()
        generateToken()
        generatePriceDetail()
        generateFooter()

        for (string in mContents) {
            Log.d("RECEIPT", string.content)
        }
    }

    private fun generateHeader(transNo: String) {

        val header = ArrayList<ReceiptString>()

        header.add(ReceiptString("Sale-Receipt", 30, Printer.Align.CENTER, true, false))
        header.add(ReceiptString(" ", 30, Printer.Align.CENTER, false, false))
        header.add(ReceiptString("Kudo Store", 24, Printer.Align.CENTER, true, false))
        header.add(ReceiptString("Kudoplex2", 24, Printer.Align.CENTER, false, false))
        header.add(ReceiptString("Radio Dalam", 24, Printer.Align.CENTER, false, false))
        header.add(ReceiptString("Jaksel", 24, Printer.Align.CENTER, false, false))
        header.add(ReceiptString(" ", 30, Printer.Align.CENTER, false, false))
        header.add(ReceiptString("PLN PRABAYAR", 24, Printer.Align.LEFT, false, false))
        header.add(ReceiptString("#" + transNo, 24, Printer.Align.LEFT, false, false))
        header.add(ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false))

        mContents.addAll(header)
    }

    private fun generateDateAndTime() {
        mContents.add(ReceiptString("30-11-2017 12:00:30", 24, Printer.Align.LEFT, false, false))
    }

    private fun generateCustomerDetail() {
        mContents.add(ReceiptString("NAMA : ADRIAN", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("NO.  : 543123456789", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false))

    }

    private fun generatePlnDetail() {
        mContents.add(ReceiptString("REF : 1IHBI3451IHB6JHB7HG3JHB2BV", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Tarif/Daya : R1/1300 VA", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("NO. Meter : 32021234567", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("KWH. : 133.2", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false))
    }

    private fun generateToken() {
        mContents.add(ReceiptString("TOKEN : 1234-1234-1234-1234-1234", 24, Printer.Align.LEFT, true, false))
        mContents.add(ReceiptString("--------------------------------------------", 30, Printer.Align.CENTER, false, false))
    }

    private fun generatePriceDetail() {
        mContents.add(ReceiptString("Biaya tagihan : Rp 202.000", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Biaya admin : Rp 2.000", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Materai : Rp 0", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Ppn : Rp 0", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Ppj : Rp 4.688", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Angsuran : Rp 0", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString("Stroom/Token : Rp 195.312", 24, Printer.Align.LEFT, false, false))
        mContents.add(ReceiptString(" ", 30, Printer.Align.CENTER, false, false))

    }

    private fun generateFooter() {
        mContents.add(ReceiptString("APPROVED", 24, Printer.Align.CENTER, true, false))
        mContents.add(ReceiptString("Thank You", 24, Printer.Align.CENTER, true, false))
    }

}