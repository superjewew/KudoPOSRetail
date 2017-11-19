package com.bountyhunter.kudo.kudoposretail.util;

import android.text.TextUtils;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * Created by adrian on 11/19/17.
 */
public final class NumberUtils {

    private static final Locale LOCALE = new Locale("in", "ID");

    public static String formatNumber(float number) {
        NumberFormat nf = NumberFormat.getInstance(LOCALE);
        return nf.format(number);
    }

    public static String formatPercentage(float number) {
        NumberFormat nf = NumberFormat.getPercentInstance(LOCALE);
        return nf.format(number);
    }

    public static String formatPrice(double price) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(LOCALE);
        return nf.format(price);
    }

    public static String formatFixedLengthNumber(int number, int length){
        String result = String.valueOf(number);
        if (result.length() < length){
            String padding;
            StringBuilder stringBuilder = new StringBuilder();
            int paddingLength = length - result.length();
            while (paddingLength-- > 0){
                stringBuilder.append("0");
            }
            padding = stringBuilder.toString();
            result = TextUtils.concat(padding, result).toString();
            return result;
        } else {
            return result;
        }
    }
}