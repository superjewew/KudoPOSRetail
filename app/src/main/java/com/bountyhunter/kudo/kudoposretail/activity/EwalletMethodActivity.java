package com.bountyhunter.kudo.kudoposretail.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bountyhunter.kudo.kudoposretail.R;

import net.glxn.qrgen.android.QRCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_ewallet_method)
public class EwalletMethodActivity extends AppCompatActivity {

    @ViewById(R.id.iv_qr_code)
    ImageView mQrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @AfterViews
    void init() {
        generateQrCode("www.google.com");
    }

    private void generateQrCode(String url) {
        Bitmap qrCode = QRCode.from(url).bitmap();
        mQrCodeImageView.setImageBitmap(qrCode);
    }
}
