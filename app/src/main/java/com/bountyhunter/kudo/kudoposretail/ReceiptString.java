package com.bountyhunter.kudo.kudoposretail;

import wangpos.sdk4.libbasebinder.Printer;
import wangpos.sdk4.libbasebinder.Printer.Align;

/**
 * Created by norman on 11/18/17.
 */

public class ReceiptString {
    private String mContent;
    private int mFontSize;
    private Align mAlignment;
    private boolean mBold;
    private boolean mItalic;

    public ReceiptString(String content, int fontSize, Align alignment, boolean bold, boolean italic) {
        mContent = content;
        mFontSize = fontSize;
        mAlignment = alignment;
        mBold = bold;
        mItalic = italic;
    }

    public String getContent() {
        return mContent;
    }

    public int getFontSize() {
        return mFontSize;
    }

    public Align getAlignment() {
        return mAlignment;
    }

    public boolean isBold() {
        return mBold;
    }

    public boolean isItalic() {
        return mItalic;
    }
}
