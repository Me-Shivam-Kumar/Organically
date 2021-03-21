package com.organically.organically;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;
    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }
    void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progree_dialog,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();

    }
    void hideDialog(){
        dialog.dismiss();
    }
}
