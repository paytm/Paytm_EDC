package com.paytm.sampledeeplinkapp;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AppInvokeWorker extends Worker {
    private final String EDC_PACKAGE = "com.paytm.pos.debug";
    Context workerContext;

    public AppInvokeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        workerContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        startEDCApp(getInputData().getString("deeplink"));
        return Result.success();
    }

    private void startEDCApp(String deepLink) {
        Intent launchIntent = workerContext.getPackageManager().getLaunchIntentForPackage(EDC_PACKAGE);
        if (launchIntent != null) {
            launchIntent.putExtra("deeplink", deepLink);
            workerContext.startActivity(launchIntent);
        }
    }
}
