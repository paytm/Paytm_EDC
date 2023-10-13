package com.paytm.ecr.bluetooth.sample;

import android.app.Application;

import com.paytm.ecr.bluetooth.sdk.IPayments;
import com.paytm.ecr.bluetooth.sdk.PaytmPayments;
import com.paytm.ecr.bluetooth.sdk.model.Config;

/**
 * This is a sample Application which provides a basic use of Lifecycle tracker
 *
 * @see com.paytm.ecr.bluetooth.sample.ui.BaseActivity
 * @see LifeCycleTracker
 */
public class SampleApp extends Application {

    private static SampleApp instance;

    private IPayments payments;

    private LifeCycleTracker lifeCycleTracker;

    public static SampleApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        payments = PaytmPayments.with(this);
        payments.init(new Config.Builder()
                .setStatusCheckOnSaleRequestEnabled(true).build());

        lifeCycleTracker = new LifeCycleTracker();
        registerActivityLifecycleCallbacks(lifeCycleTracker);
    }

    public IPayments getPayments() {
        return payments;
    }

    public LifeCycleTracker getLifeCycleTracker() {
        return lifeCycleTracker;
    }

}
