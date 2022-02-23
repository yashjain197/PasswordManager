package com.passwordmanager.passwordmanager.ProgressBar;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.passwordmanager.passwordmanager.R;

public class ProgressBar {
    private Activity activity;
    private AlertDialog dialog;

    public ProgressBar(Activity myActivity) {
        activity = myActivity;
    }
    public void startLoadingDialog(){
        AlertDialog.Builder builder =new AlertDialog.Builder(activity);
        LayoutInflater inflater;
        inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_bar,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();

    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
