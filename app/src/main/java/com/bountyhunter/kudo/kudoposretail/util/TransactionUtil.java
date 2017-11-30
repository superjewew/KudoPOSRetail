package com.bountyhunter.kudo.kudoposretail.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.METHOD_CARD;
import static com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.METHOD_CASH;
import static com.bountyhunter.kudo.kudoposretail.receipt.BaseReceipt.METHOD_EWALLET;

/**
 * Created by norman on 11/22/17.
 */

public class TransactionUtil {

    public static String generateTransNo(int method) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Random random = new Random();
        int ran = 10 + random.nextInt(89);

        return dateFormat.format(cal.getTime()) + "1" + ran + "" + method;
    }

    public static int determinePaymentMethodFromTransNo(String transNo) {
        int method = Integer.valueOf(transNo.substring(transNo.length() - 1));
        switch(method) {
            case METHOD_EWALLET:
                return 0;
            case METHOD_CARD:
                return 1;
            case METHOD_CASH:
                return 2;
        }
        return 2;
    }
}
