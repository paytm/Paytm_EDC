package com.paytm.sampledeeplinkapp;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    public static String request;
    public static String response;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    protected void showDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_req_resp, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((TextView) view.findViewById(R.id.req)).setText(String.format("Request : %s", request));
        ((TextView) view.findViewById(R.id.resp)).setText(String.format("Response : %s", response));

        dialog.show();
    }
}
