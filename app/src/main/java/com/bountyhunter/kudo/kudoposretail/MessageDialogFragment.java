package com.bountyhunter.kudo.kudoposretail;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by norman on 11/19/17.
 */

public class MessageDialogFragment extends DialogFragment {

    DialogClickListener mListener;

    public static MessageDialogFragment newInstance(int title, DialogClickListener listener) {
        MessageDialogFragment frag = new MessageDialogFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        frag.mListener = listener;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton(R.string.alert_dialog_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onPositiveClicked();
                            }
                        }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onNegativeClicked();
                            }
                        }
                )
                .create();
    }

    public interface DialogClickListener {
        void onPositiveClicked();
        void onNegativeClicked();
    }
}
