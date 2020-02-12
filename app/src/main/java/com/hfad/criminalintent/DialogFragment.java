package com.hfad.criminalintent;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

public class DialogFragment extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }


}
