package com.quoctrungdhqn.shiportalandroid.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.quoctrungdhqn.shiportalandroid.R;

public class DialogUtils {
    public static void showBasicDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        if (title != null) {
            builder.setTitle(title);
        } else {
            builder.setTitle(R.string.dialog_error_title);
        }
        if (message != null) {
            builder.setMessage(message);
        } else {
            builder.setMessage(R.string.message_dialog_error);
        }
        builder.setPositiveButton(R.string.btn_okay, listener);

        builder.create().show();
    }

    public static AlertDialog getLoadingDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        //TODO create dialog builder
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_waiting_dialog, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }
}
