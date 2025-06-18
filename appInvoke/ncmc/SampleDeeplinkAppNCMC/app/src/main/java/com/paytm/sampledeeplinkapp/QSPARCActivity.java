package com.paytm.sampledeeplinkapp;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.concurrent.TimeUnit;

public class QSPARCActivity extends BaseActivity implements View.OnClickListener {

    private final String callBackAction = "com.paytm.pos.payment.CALL_BACK_RESULT_2";
    //private final String EDC_PACKAGE = "com.paytm.pos";
    private final String EDC_PACKAGE = "com.paytm.pos.debug";

    private Button btn_check_balance;
    private Button btn_update_balance;
    private Button btn_read;
    private Button btn_cash;
    private Button btn_account;

    private EditText amountText;
    private EditText tags;

    private EditText orderId_balance;
    private EditText orderId_add_money;
    private EditText orderId_read;

    private EditText timer_balance;
    private long timer = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qsparc);

        btn_check_balance = findViewById(R.id.btn_check_balance);
        btn_update_balance = findViewById(R.id.btn_update_balance);
        btn_read = findViewById(R.id.btn_read);
        btn_cash = findViewById(R.id.btn_cash);
        btn_account = findViewById(R.id.btn_account);

        amountText = findViewById(R.id.amount);
        tags = findViewById(R.id.tags);

        orderId_balance = findViewById(R.id.orderId_balance);
        orderId_add_money = findViewById(R.id.orderId_add_money);
        orderId_read = findViewById(R.id.orderId_read);

        timer_balance = findViewById(R.id.timer_balance);


        btn_check_balance.setOnClickListener(this);
        btn_update_balance.setOnClickListener(this);
        btn_read.setOnClickListener(this);
        btn_cash.setOnClickListener(this);
        btn_account.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.hasExtra("deeplink")) {
            String dl = intent.getStringExtra("deeplink");
            Log.i("SampleDeeplink", "Response Deeplink : " + dl);
            response = dl;
            showDialog();
            Uri uri = Uri.parse(dl);

            String amount = uri.getQueryParameter("amount");
            String orderId = uri.getQueryParameter("orderId");

            String packageName = getPackageName();
            Log.i("QSparc", "Response" + uri.toString());
        }
    }

    @Override
    public void onClick(View v) {
        //timer = Long.parseLong(timer_balance.getText().toString().trim());
        switch (v.getId()) {
            case R.id.btn_check_balance:
                String orderId = orderId_balance.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                String deepLink = "paytmedc://checkBalanceWithData?" + "callbackAction=" + callBackAction + "&stackClear=false" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://checkBalance";
                deepLink += "&orderId=" + orderId;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }
                deepLink += "&requestTags=" + "5A, DF33";
                startEDCApp(deepLink);

                break;
            case R.id.btn_update_balance:
                orderId = orderId_balance.getText().toString().trim();
                String serviceId = timer_balance.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                deepLink = "paytmedc://updateBalance?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://updateBalance";
                //deepLink += "&orderId=" + orderId;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&serviceId=" + serviceId;
                }
                //deepLink += "&cardNoSHA=" + "9c420fd342ab7725d42078dc4fa07c194bb2b6c347ef4a8b84b0d206be5b300fde1d4bd1ba0c6c5ff6cca3d66619598e53aafff2f065ca594b6f378bea236f12";
                startEDCApp(deepLink);
                break;
            case R.id.btn_read:
                orderId = orderId_read.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                String tagsText = tags.getText().toString().trim();
                /*deepLink = "paytmedc://readCard?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://readCard";
                *//*if (!TextUtils.isEmpty(tagsText)) {
                    deepLink += "&requestTags=" + tagsText;
                }*//*
                //deepLink += "&orderId=" + orderId;
                deepLink += "&cardMfType=" + orderId;
                deepLink += "&flowType=" + tagsText;*/
                deepLink = "paytmedc://readCard?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://readCard&orderId=12345&flowType=closed&cardMfType=MF&callbackIsService=false";
                startEDCApp(deepLink);
                break;
            /*case R.id.btn_read:
                orderId = orderId_read.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                deepLink = "paytmedc://readClosedCard?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://readCard";
                //deepLink += "&orderId=" + orderId;
                deepLink += "&cardType=" + orderId;

                startEDCApp(deepLink);
                break;*/
            case R.id.btn_cash:
                orderId = orderId_add_money.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                String amount = amountText.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || amount.length() < 3) {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
                deepLink = "paytmedc://addMoney?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://addMoney";
                //deepLink += "&orderId=" + orderId;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }
                deepLink += "&amount=" + amount;
                deepLink += "&serviceType=" + "CASH";
                //deepLink += "&cardNoSHA=" + "9879bf9751c001b8fba2acc97027ab100abe77fe0ac2dc54b3fd8a5d1de29c97";
                //deepLink += "&cardNoSHA=" + "5f1922264346c999a8c25274d4f89bbf4de596675f90006e822b648db6e7c556c3bd3b187a6593e7d9d4dd478427c15d2c2976959516959b1461902c0f7c1a46";
                //deepLink += "&cardNoSHA=" + "9c420fd342ab7725d42078dc4fa07c194bb2b6c347ef4a8b84b0d206be5b300fde1d4bd1ba0c6c5ff6cca3d66619598e53aafff2f065ca594b6f378bea236f12";
                startEDCApp(deepLink);
                break;
            case R.id.btn_account:
                orderId = orderId_add_money.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                amount = amountText.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || amount.length() < 3) {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                deepLink = "paytmedc://addMoney?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://addMoney";
                //deepLink += "&orderId=" + orderId;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }
                deepLink += "&amount=" + amount;
                deepLink += "&serviceType=" + "ACCOUNT";
                //deepLink += "&cardNoSHA=" + "9c420fd342ab7725d42078dc4fa07c194bb2b6c347ef4a8b84b0d206be5b300fde1d4bd1ba0c6c5ff6cca3d66619598e53aafff2f065ca594b6f378bea236f12";
                startEDCApp(deepLink);
                break;
        }
    }

    private void startEDCApp(String deepLink) {
        request = deepLink;
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(EDC_PACKAGE);
        if (launchIntent != null) {
            launchIntent.putExtra("deeplink", deepLink);
            startActivity(launchIntent);
        }
        /*Data.Builder data = new Data.Builder();
        data.putString("deeplink", deepLink);

        OneTimeWorkRequest workerRequest = new OneTimeWorkRequest.Builder(AppInvokeWorker.class).
                setInitialDelay(timer, TimeUnit.SECONDS).setInputData(data.build()).build();
        WorkManager.getInstance(this).enqueue(workerRequest);
        timer = 0;*/
    }

    private void startDMRCService(){
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.ridlr.dmrc.transit",
                "com.ridlr.dmrc.transit.services.DMRCTransitConnectService"));
        this.startService(intent);
    }
}
