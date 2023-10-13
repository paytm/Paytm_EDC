package com.paytm.ecr.bluetooth.sample;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * This Class can be used for managing connection lifecycle aware.
 * This enables the application to open and close connection,
 * as the application comes to foreground and goes in background respectively
 *
 * @see SampleApp
 * @see com.paytm.ecr.bluetooth.sample.ui.BaseActivity
 */
public class LifeCycleTracker implements Application.ActivityLifecycleCallbacks {

    private int activityCount = 0;

    private boolean appStopped = true;

    private LifeCycleListener lifeCycleListener = null;


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activityCount += 1;
        if (activityCount == 1) {
            if (appStopped) {
                if (lifeCycleListener != null) {
                    lifeCycleListener.comingToForeGround(true);
                }
                appStopped = false;
            } else {
                if (lifeCycleListener != null) {
                    lifeCycleListener.comingToForeGround(false);
                }
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityCount -= 1;
        if (activityCount == 0) {
            appStopped = activity.isFinishing();
            if (lifeCycleListener != null) {
                lifeCycleListener.goingToBackground(appStopped);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public boolean isAppInForeground() {
        return activityCount > 0;
    }

    public void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
        this.lifeCycleListener = lifeCycleListener;
    }

    public interface LifeCycleListener {

        void goingToBackground(boolean isAppFinishing);

        void comingToForeGround(boolean isAppStarting);
    }
}
