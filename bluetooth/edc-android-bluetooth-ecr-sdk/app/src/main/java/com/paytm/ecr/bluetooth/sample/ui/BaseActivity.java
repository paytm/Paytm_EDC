package com.paytm.ecr.bluetooth.sample.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paytm.ecr.bluetooth.sample.LifeCycleTracker;
import com.paytm.ecr.bluetooth.sample.SampleApp;
import com.paytm.ecr.bluetooth.sdk.IConnectionStateListener;
import com.paytm.ecr.bluetooth.sdk.IPayments;
import com.paytm.ecr.bluetooth.sdk.constants.ConnectionError;
import com.paytm.ecr.bluetooth.sdk.constants.ConnectionState;

/**
 * Works in parallel with Lifecycle tracker to maintain connection lifecycle if required
 *
 * @see SampleApp
 * @see LifeCycleTracker
 */
public class BaseActivity extends AppCompatActivity implements LifeCycleTracker.LifeCycleListener, IConnectionStateListener {

    protected IPayments payments = SampleApp.getInstance().getPayments();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SampleApp.getInstance().getLifeCycleTracker().setLifeCycleListener(this);
    }

    @Override
    public void goingToBackground(boolean isAppFinishing) {
        if (isAppFinishing) {
            SampleApp.getInstance().getLifeCycleTracker().setLifeCycleListener(null);
            //Connection can be closed here
            //payments.closeConnection();
        }
    }

    @Override
    public void comingToForeGround(boolean isAppStarting) {
//          Connection can be reopened here
//        if (!payments.isConnected()) {
//            payments.openConnection(this);
//        }
    }

    /**
     * These callbacks can be used to keep track of connection state in application
     * @see ConnectionState
     * @see ConnectionError
     */
    @Override
    public void onStatusUpdated(ConnectionState state) {
        Toast.makeText(getApplicationContext(), state.name(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(ConnectionError error) {
        Toast.makeText(getApplicationContext(), error.name(), Toast.LENGTH_SHORT).show();
    }
}
