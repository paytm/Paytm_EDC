package com.paytm.androidserialcommunication;

import android.app.Application;

import com.paytm.androidecrsdk.IPayments;
import com.paytm.androidecrsdk.PaytmPayments;
import com.paytm.androidecrsdk.model.Config;
import com.paytm.androidserialcommunication.ui.BaseActivity;

/**
 * This is a sample Application which provides a basic use of Lifecycle tracker
 *
 * @see BaseActivity
 * @see LifeCycleTracker
 */
public class SampleApp extends Application {

    private static SampleApp instance;

    private IPayments payments;

    private LifeCycleTracker lifeCycleTracker;

    private boolean syncMode = true;

    public static SampleApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initPayments(true);
        lifeCycleTracker = new LifeCycleTracker();
        registerActivityLifecycleCallbacks(lifeCycleTracker);
    }

    public void initPayments(boolean syncMode) {
        this.syncMode = syncMode;
        payments = PaytmPayments.with(this);
        payments.init(new Config.Builder()
                .setStatusCheckOnSaleRequestEnabled(this.syncMode).build());
    }

    public IPayments getPayments() {
        return payments;
    }

    public LifeCycleTracker getLifeCycleTracker() {
        return lifeCycleTracker;
    }

    public boolean isSyncMode() {
        return syncMode;
    }
}
