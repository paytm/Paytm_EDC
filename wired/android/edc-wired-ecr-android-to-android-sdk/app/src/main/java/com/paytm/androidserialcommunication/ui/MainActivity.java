package com.paytm.androidserialcommunication.ui;


import static com.paytm.androidserialcommunication.Constants.CANCEL;
import static com.paytm.androidserialcommunication.Constants.CONNECTION_CHECK;
import static com.paytm.androidserialcommunication.Constants.PRINT;
import static com.paytm.androidserialcommunication.Constants.SALE;
import static com.paytm.androidserialcommunication.Constants.STATUS_CHECK;
import static com.paytm.androidserialcommunication.Constants.TXN_TYPE;
import static com.paytm.androidserialcommunication.Constants.VOID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.paytm.androidecrsdk.IPayments;
import com.paytm.androidecrsdk.util.DeviceStateCode;
import com.paytm.androidserialcommunication.R;
import com.paytm.androidserialcommunication.SampleApp;

/**
 * This is the landing activity used as interface to choose which functionality to perform
 *
 * @see IPayments
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponent();
    }

    @Override
    public void handleDeviceState(DeviceStateCode deviceStateCode) {
        super.handleDeviceState(deviceStateCode);
        ((TextView) findViewById(R.id.btn_connect)).setText(getString(R.string.connected, String.valueOf(SampleApp.getInstance().getPayments().isConnected())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.btn_connect)).setText(getString(R.string.connected, String.valueOf(SampleApp.getInstance().getPayments().isConnected())));
    }

    private void initComponent() {

//        Button mInitBtn = findViewById(R.id.btn_init);
        Button mSaleBtn = findViewById(R.id.btn_sale);
        Button mStatusCheckBtn = findViewById(R.id.btn_statuscheck);
        Button mCancelBtn = findViewById(R.id.btn_cancel);
        Button mPrintBtn = findViewById(R.id.btn_print);
        Button mVoidBtn = findViewById(R.id.btn_void);
        Button mConnectionCheckBtn = findViewById(R.id.btn_connnection_check);
//        Button mPreAuthBtn = findViewById(R.id.btn_preauth);
//        Button mCaptureBtn = findViewById(R.id.btn_capture);
//        Button mReleaseBtn = findViewById(R.id.btn_release);
//        Button mSyncToggleBtn = findViewById(R.id.btn_toggle_sync_mode);
        Button mConnectBtn = findViewById(R.id.btn_connect);

//        mInitBtn.setOnClickListener(onClickListener);
        mSaleBtn.setOnClickListener(onClickListener);
        mStatusCheckBtn.setOnClickListener(onClickListener);
        mCancelBtn.setOnClickListener(onClickListener);
        mPrintBtn.setOnClickListener(onClickListener);
        mVoidBtn.setOnClickListener(onClickListener);
        mConnectionCheckBtn.setOnClickListener(onClickListener);
//        mPreAuthBtn.setOnClickListener(onClickListener);
//        mCaptureBtn.setOnClickListener(onClickListener);
//        mReleaseBtn.setOnClickListener(onClickListener);
//        mSyncToggleBtn.setOnClickListener(onClickListener);
        mConnectBtn.setOnClickListener(onClickListener);

        mConnectBtn.setText(getString(R.string.connected, String.valueOf(SampleApp.getInstance().getPayments().isConnected())));
    }

    private final View.OnClickListener onClickListener = view -> {
        if (R.id.btn_sale == view.getId()) {
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
        } /*else if (R.id.btn_preauth == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, PREAUTH);
            startActivity(intent);
        } else if (R.id.btn_capture == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, CAPTURE);
            startActivity(intent);
        } else if (R.id.btn_release == view.getId()) {
            Intent intent = new Intent(this, TransactionActivity.class);
            intent.putExtra(TXN_TYPE, RELEASE);
            startActivity(intent);
        } else if (R.id.btn_toggle_sync_mode == view.getId()) {
            boolean toggleState = !SampleApp.getInstance().isSyncMode();
            SampleApp.getInstance().initPayments(toggleState);
            ((TextView) view).setText(getString(R.string.sync_mode_s, String.valueOf(SampleApp.getInstance().isSyncMode())));
        }*/ else if (R.id.btn_connect == view.getId()) {
            IPayments iPayments = SampleApp.getInstance().getPayments();
            boolean isConnected = iPayments.isConnected();
            if (isConnected) {
                iPayments.disConnect();
            } else {
                iPayments.connect();
            }
        }
    };
}