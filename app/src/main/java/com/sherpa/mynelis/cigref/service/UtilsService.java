package com.sherpa.mynelis.cigref.service;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.sherpa.mynelis.cigref.R;

/**
 * Created by Alexis Largaiolli on 20/10/17.
 */

public class UtilsService {

    public static void showErrorAlert(final Context context, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(true).setTitle(context.getString(R.string.error_alert_title)).setMessage(message).show();
    }

    public static void showWarningAlert(final Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(true).setTitle(title).setMessage(message).show();
    }

    public static void showInfoAlert(final Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, R.style.DialogStyle);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setCancelable(true).setTitle(title).setMessage(message).show();
    }

}
