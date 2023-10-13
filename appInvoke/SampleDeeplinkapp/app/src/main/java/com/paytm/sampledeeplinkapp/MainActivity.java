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

public class MainActivity extends BaseActivity {


    private Button btn_pay;
    private Button btn_qr_pay;
    private Button read_card;
    private Button void_transaction;
    private Button btn_status;
    private Button btn_new_pay;
    private Button btn_v2;

    private EditText orderIdText;
    private EditText amountText;

    private EditText stanText;
    private EditText rrnText;

    private String orderId;
    private String amount;
    private String stan;
    private String rrn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_pay = findViewById(R.id.btn_pay);
        btn_qr_pay = findViewById(R.id.btn_qrPay);
        read_card = findViewById(R.id.read_card);
        void_transaction = findViewById(R.id.void_transaction);
        btn_status = findViewById(R.id.btn_status);
        btn_new_pay = findViewById(R.id.btn_new_pay);
        orderIdText = findViewById(R.id.orderId);
        amountText = findViewById(R.id.amount);
        stanText = findViewById(R.id.stan);
        rrnText = findViewById(R.id.rrn);
        btn_v2 = findViewById(R.id.btn_v2);

        if (getIntent() != null && getIntent().hasExtra("deeplink")) {
            String dl = getIntent().getStringExtra("deeplink");
            response = dl;
            showDialog();
            Uri uri = Uri.parse(dl);

            String amount = uri.getQueryParameter("amount");
            String orderId = uri.getQueryParameter("orderId");
            Log.i("SampleDeeplink", dl);
        }

        btn_v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeepLinkV2Activity.class));
            }
        });

        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stan = stanText.getText().toString().trim();
                    rrn = rrnText.getText().toString().trim();

                    if (TextUtils.isEmpty(stan) && TextUtils.isEmpty(rrn)) {
                        Toast.makeText(MainActivity.this, "Please enter valid STAN or RRN", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    btn_status.setText("Clicked check status");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos.debug");
                    if (launchIntent != null) {
                        request = "paytmedc://txnStatus?stan=" + stan + "&RRN=" + rrn +
                                "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://status";
                        launchIntent.putExtra("deeplink", request);
                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderId = orderIdText.getText().toString().trim();
                amount = amountText.getText().toString().trim();

                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(MainActivity.this, "Invalid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(amount) || amount.length() < 3 || amount.length() > 9) {
                    Toast.makeText(MainActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    btn_pay.setText("Clicked Payment");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos");
                    if (launchIntent != null) {
                        String deepLink = "paytmedc://payment?orderId=" + orderId + "&amount=" + amount + "&invoiceId=pos34561237&paymentRefNo=672345435" +
                                "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://pay";
                        request = deepLink;
                        deepLink = "paytmedc://payment?amount=100&orderId=SHAHWS1276_qa&invoiceId=SHAHWS1276_qa&paymentRefNo=SHAHWS1276_qa&stackClear=false&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=paytm://success";
                        Log.i("SampleDeeplink", "Request Deeplink : " + deepLink);
                        launchIntent.putExtra("deeplink", deepLink);
                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        btn_new_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = orderIdText.getText().toString().trim();
                amount = amountText.getText().toString().trim();

                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(MainActivity.this, "Invalid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(amount) || amount.length() < 3 || amount.length() > 9) {
                    Toast.makeText(MainActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    btn_new_pay.setText("Clicked New Payment");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos.debug");
                    if (launchIntent != null) {
                        String deepLink = "paytmedc://paymentAll?orderId=" + orderId + "&amount=" + amount + "&invoiceId=pos34561237&paymentRefNo=672345435" +
                                "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://payAll";
                        request = deepLink;
                        Log.i("SampleDeeplink", "Request Deeplink : " + deepLink);
                        launchIntent.putExtra("deeplink", deepLink);
                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        btn_qr_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderId = orderIdText.getText().toString().trim();
                amount = amountText.getText().toString().trim();

                if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(MainActivity.this, "Invalid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(amount) || amount.length() < 3 || amount.length() > 9) {
                    Toast.makeText(MainActivity.this, "Invalid amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    btn_qr_pay.setText("Clicked QR Payment");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos.debug");
                    if (launchIntent != null) {
                        String deepLink = "paytmedc://qrPayment?amount=" + amount + "&orderId=" + orderId + "&invoiceId=pos34561237&paymentRefNo=672345435" +
                                "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://qrPay";
                        request = deepLink;
                        Log.i("SampleDeeplink", "Request Deeplink : " + deepLink);
                        launchIntent.putExtra("deeplink", deepLink);
                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        read_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    read_card.setText("Clicked Read Card");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos.debug");
                    if (launchIntent != null) {
                        request = "paytmedc://readCard?readMethod=tap&stackClear=true&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://readCard";
                        launchIntent.putExtra("deeplink", request);

                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        void_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    stan = stanText.getText().toString().trim();

                    if (TextUtils.isEmpty(stan)) {
                        Toast.makeText(MainActivity.this, "Please enter valid STAN", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    void_transaction.setText("Clicked Void");
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.paytm.pos.debug");
                    if (launchIntent != null) {
                        request = "paytmedc://void?" +
                                "stan=" + stan + "&stackClear=true&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://void";
                        launchIntent.putExtra("deeplink", request);

                        startActivity(launchIntent);//null pointer check in case package name was not found
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


    }
}
