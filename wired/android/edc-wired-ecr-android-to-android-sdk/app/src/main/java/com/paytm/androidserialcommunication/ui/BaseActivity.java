package com.paytm.androidserialcommunication.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paytm.androidecrsdk.IPayments;
import com.paytm.androidecrsdk.util.DeviceStateCode;
import com.paytm.androidserialcommunication.LifeCycleTracker;
import com.paytm.androidserialcommunication.SampleApp;


/**
 * Works in parallel with Lifecycle tracker to maintain connection lifecycle if required
 *
 * @see SampleApp
 * @see LifeCycleTracker
 */
public class BaseActivity extends AppCompatActivity implements LifeCycleTracker.LifeCycleListener {

    protected IPayments payments = SampleApp.getInstance().getPayments();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SampleApp.getInstance().getLifeCycleTracker().setLifeCycleListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        payments.setDeviceStateListener(deviceStateCode -> runOnUiThread(() -> handleDeviceState(deviceStateCode)));
    }

    @Override
    public void goingToBackground(boolean isAppFinishing) {
        if (isAppFinishing) {
            SampleApp.getInstance().getLifeCycleTracker().setLifeCycleListener(null);
            // Connection can be closed here
            payments.setDeviceStateListener(null);
            payments.disConnect();
        }
    }

    @Override
    public void comingToForeGround(boolean isAppStarting) {
        // Connection can be reopened here
        if (isAppStarting) {
            payments.connect();
        }
    }

    public void handleDeviceState(DeviceStateCode deviceStateCode) {
        Toast.makeText(this, "Device State: " + deviceStateCode.name(), Toast.LENGTH_SHORT).show();
    }
}
