package com.paytm.ecr.bluetooth.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paytm.ecr.bluetooth.sample.R;
import com.paytm.ecr.bluetooth.sample.SampleApp;

import static com.paytm.ecr.bluetooth.sample.Constants.CANCEL;
import static com.paytm.ecr.bluetooth.sample.Constants.CONNECTION_CHECK;
import static com.paytm.ecr.bluetooth.sample.Constants.PRINT;
import static com.paytm.ecr.bluetooth.sample.Constants.SALE;
import static com.paytm.ecr.bluetooth.sample.Constants.STATUS_CHECK;
import static com.paytm.ecr.bluetooth.sample.Constants.TXN_TYPE;
import static com.paytm.ecr.bluetooth.sample.Constants.VOID;

/**
 * This is the landing activity used as interface to choose which functionality to perform
 *
 * @see com.paytm.ecr.bluetooth.sdk.IPayments
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    private void initComponent() {

        Button mInitBtn = findViewById(R.id.btn_init);
        Button mSettingsBtn = findViewById(R.id.btn_settings);
        Button mSaleBtn = findViewById(R.id.btn_sale);
        Button mStatusCheckBtn = findViewById(R.id.btn_statuscheck);
        Button mCancelBtn = findViewById(R.id.btn_cancel);
        Button mPrintBtn = findViewById(R.id.btn_print);
        Button mVoidBtn = findViewById(R.id.btn_void);
        Button mConnectionCheckBtn = findViewById(R.id.btn_connnection_check);

        mInitBtn.setOnClickListener(onClickListener);
        mSettingsBtn.setOnClickListener(onClickListener);
        mSaleBtn.setOnClickListener(onClickListener);
        mStatusCheckBtn.setOnClickListener(onClickListener);
        mCancelBtn.setOnClickListener(onClickListener);
        mPrintBtn.setOnClickListener(onClickListener);
        mVoidBtn.setOnClickListener(onClickListener);
        mConnectionCheckBtn.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = view -> {
        if (R.id.btn_init == view.getId()) {
            boolean initialised = SampleApp.getInstance().getPayments() != null;
            String message = initialised ? "Initialized" : "Not initialized";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else if (R.id.btn_settings == view.getId()) {
            payments.openBluetoothSettings();
        } else if (R.id.btn_sale == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, SALE);
            startActivity(intent);
        } else if (R.id.btn_statuscheck == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, STATUS_CHECK);
            startActivity(intent);
        } else if (R.id.btn_cancel == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, CANCEL);
            startActivity(intent);
        } else if (R.id.btn_print == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, PRINT);
            startActivity(intent);
        } else if (R.id.btn_void == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, VOID);
            startActivity(intent);
        } else if (R.id.btn_connnection_check == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, CONNECTION_CHECK);
            startActivity(intent);
        }
    };
}