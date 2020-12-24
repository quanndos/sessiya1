package com.example.s1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class Oshibka extends DialogFragment {

    private String warning;

    public Oshibka(String warning){
        this.warning = warning;
    }

    public Dialog onCreateDialog(Bundle saveInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder
                .setTitle("Ошибка")
                .setMessage(warning)
                .setPositiveButton("Ок", null)
                .create();
    }
}
