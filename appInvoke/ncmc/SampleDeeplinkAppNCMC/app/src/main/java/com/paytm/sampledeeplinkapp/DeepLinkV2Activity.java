package com.paytm.sampledeeplinkapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeepLinkV2Activity extends BaseActivity implements View.OnClickListener {

    private final String callBackAction = "com.paytm.pos.payment.CALL_BACK_RESULT";
    private final String EDC_PACKAGE = "com.paytm.pos.debug";

    private Button btn_pay;
    private Button btn_check;
    private Button btn_void;

    private EditText amountText;
    private EditText pay_mode;

    private EditText orderId_pay;
    private EditText orderId_status;
    private EditText orderId_void;

    private EditText param1_pay;
    private EditText param2_pay;

    private EditText param1_status;
    private EditText param2_status;

    private EditText param1_void;
    private EditText param2_void;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink_v2);

        btn_pay = findViewById(R.id.btn_pay);
        btn_check = findViewById(R.id.btn_check);
        btn_void = findViewById(R.id.btn_void);

        amountText = findViewById(R.id.amount);
        pay_mode = findViewById(R.id.pay_mode);

        orderId_pay = findViewById(R.id.orderId_pay);
        orderId_status = findViewById(R.id.orderId_status);
        orderId_void = findViewById(R.id.orderId_void);

        param1_pay = findViewById(R.id.param1_pay);
        param2_pay = findViewById(R.id.param2_pay);

        param1_status = findViewById(R.id.param1_status);
        param2_status = findViewById(R.id.param2_status);

        param1_void = findViewById(R.id.param1_void);
        param2_void = findViewById(R.id.param2_void);

        btn_pay.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        btn_void.setOnClickListener(this);
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pay:
                String orderId = orderId_pay.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                String amount = amountText.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || amount.length() < 3) {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                String deepLink = "paytmedc://paymentV2?" + "callbackAction=" + callBackAction + "&stackClear=false" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://payment";
                deepLink += "&amount=" + amount;
                deepLink += "&orderId=" + orderId;
                String payMode = pay_mode.getText().toString().trim();
                if (!TextUtils.isEmpty(payMode)) {
                    deepLink += "&requestPayMode=" + payMode;
                }

                String param1 = param1_pay.getText().toString().trim();
                if (!TextUtils.isEmpty(param1)) {
                    deepLink += "&param1=" + param1;
                }
                String param2 = param2_pay.getText().toString().trim();
                if (!TextUtils.isEmpty(param2)) {
                    deepLink += "&param2=" + param2;
                }
                startEDCApp(deepLink);

                break;
            case R.id.btn_check:
                orderId = orderId_status.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                deepLink = "paytmedc://txnStatusV2?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://txnStatus";
                deepLink += "&orderId=" + orderId;
                param1 = param1_status.getText().toString().trim();
                if (!TextUtils.isEmpty(param1)) {
                    deepLink += "&param1=" + param1;
                }
                param2 = param2_status.getText().toString().trim();
                if (!TextUtils.isEmpty(param2)) {
                    deepLink += "&param2=" + param2;
                }
                startEDCApp(deepLink);
                break;
            case R.id.btn_void:
                orderId = orderId_void.getText().toString().trim();
                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                deepLink = "paytmedc://voidV2?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://void";
                deepLink += "&orderId=" + orderId;
                param1 = param1_void.getText().toString().trim();
                if (!TextUtils.isEmpty(param1)) {
                    deepLink += "&param1=" + param1;
                }
                param2 = param2_void.getText().toString().trim();
                if (!TextUtils.isEmpty(param2)) {
                    deepLink += "&param2=" + param2;
                }
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
    }
}
