package com.organically.organically;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog2 {
    Activity activity;
    AlertDialog dialog;
    LoadingDialog2(Activity myActivity){
        activity=myActivity;
    }
    void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progrees_dialog_2,null));
        builder.setCancelable(true);
        dialog=builder.create();
        dialog.show();

    }
    void hideDialog(){
        dialog.dismiss();
    }
}
