package com.bountyhunter.kudo.kudoposretail.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bountyhunter.kudo.kudoposretail.MessageDialogFragment;
import com.bountyhunter.kudo.kudoposretail.R;

import net.glxn.qrgen.android.QRCode;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_ewallet_method)
public class EwalletMethodActivity extends AppCompatActivity {

    @ViewById(R.id.iv_qr_code)
    ImageView mQrCodeImageView;

    private MessageDialogFragment mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDialog = MessageDialogFragment.newInstance(R.string.title_choose_another_payment_method,
                new MessageDialogFragment.DialogClickListener() {
                    @Override
                    public void onPositiveClicked() {
                        finish();
                    }

                    @Override
                    public void onNegativeClicked() {
                        mDialog.dismiss();
                    }
                });
    }

    @AfterViews
    void init() {
        generateQrCode("www.google.com");
    }

    @Override
    public void onBackPressed() {
        mDialog.show(getFragmentManager(), "dialog");
    }

    private void generateQrCode(String url) {
        Bitmap qrCode = QRCode.from(url).bitmap();
        mQrCodeImageView.setImageBitmap(qrCode);
    }
}
