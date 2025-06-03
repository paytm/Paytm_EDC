package com.paytm.sampledeeplinkapp;

import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class TransitActivity extends BaseActivity implements View.OnClickListener {

    private final String callBackAction = "com.paytm.pos.payment.CALL_BACK_RESULT_1";
    private final String EDC_PACKAGE = "com.paytm.pos.debug";

    private Button btn_create_service;
    private Button btn_block_service;
    private Button btn_update;
    private Button btn_cash;
    private Button btn_account;

    private EditText amountText;
    private EditText tags;

    private EditText orderId_service;
    private EditText orderId_add_money;
    private EditText orderId_read;

    private EditText serviceId_service;

    private EditText orderId_update;
    private EditText service_update;
    private EditText amount_update;

    private EditText timer_service;
    private long timer = 0;

    private static final String serviceData = "0000" + "0000FF60100020C10CF494015E000000000000" + "0100010202020d1b6c0002025800726200" + "FF60100020CE0CF4180039005A00200000FF60100020CE0CF4170038005A00260000FF60100020CE0CF4160037005A002B000000000000000000";
    //private static final String serviceData = "222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222";
    //private static final String serviceData = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    //private static final String serviceData = "0205053005010111000002000102010206000A0101030201020304050607081122330102010215050110101000000000100001000000010203000102040100000000000000000000000000000000000000000000000000000000000000000000";
    //private static final String serviceData = "0106003005010111000002000102010206000A0101030201020304050607000100250102010215050110101000000000100001000000010203000102040100000000000000000000000000000000000000000000000000000000000000000001";
    //private static final String serviceData = "0106053005010111000002000102010206000A0101030201020304050607081122330102010215050110101000000000100001000000010203000102040100000000000000000000000000000000000000000000000000000000000000000000";
    //service data validation txn status 0 and 1 allowed
    /*private static final String serviceData = "3100" +
            "0000010001020144152840015E000000000010" +
            "010001020144152840000201f418416000" +
            "0100010040D915272500040064184A0000" +
            "0100010040D915271A0003006418510000" +
            "0000000000000000000000000000000000" +
            "00000000000000";*/
    //service data exit not found
    /*private static final String serviceData = "3100" +  //general info
            "00000100010040D9148909015E000000000010" +  //validation
            "01000100208207E12B0001006401388200" +  //history 1
            "0000000000000000000000000000000000" +  //history 2
            "0000000000000000000000000000000000" +  //history 3
            "0000000000000000000000000000000000" +  //history 4
            "00000000000000";                       //rfu*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);

        //service create and block
        btn_create_service = findViewById(R.id.btn_create_service);
        btn_block_service = findViewById(R.id.btn_block_service);
        orderId_service = findViewById(R.id.orderId_service);

        serviceId_service = findViewById(R.id.serviceId_service);

        /*btn_cash = findViewById(R.id.btn_cash);
        btn_account = findViewById(R.id.btn_account);

        amountText = findViewById(R.id.amount);
        tags = findViewById(R.id.tags);*/

        //service update
        orderId_update = findViewById(R.id.orderId_update);
        service_update = findViewById(R.id.service_data);
        if(!TextUtils.isEmpty(serviceData)){
            service_update.setText(serviceData);
        }
        amount_update = findViewById(R.id.amount_update);
        btn_update = findViewById(R.id.btn_update);

        btn_create_service.setOnClickListener(this);
        btn_block_service.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        timer_service = findViewById(R.id.timer_service);
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
        //timer = Long.parseLong(timer_service.getText().toString().trim());
        switch (v.getId()) {
            case R.id.btn_create_service:
                String orderId = orderId_service.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                String serviceId = serviceId_service.getText().toString().trim();
                /*if (TextUtils.isEmpty(serviceId)) {
                    Toast.makeText(this, "Please enter valid Service Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                String deepLink = "paytmedc://createService?" + "callbackAction=" + callBackAction + "&stackClear=false" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://createService";
                //deepLink += "&orderId=" + orderId;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }
                if (!TextUtils.isEmpty(serviceId)){
                    deepLink += "&serviceId=" + serviceId;
                }
                startEDCApp(deepLink);

                break;
            case R.id.btn_block_service:
                orderId = orderId_service.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                serviceId = serviceId_service.getText().toString().trim();
                if (TextUtils.isEmpty(serviceId)) {
                    Toast.makeText(this, "Please enter valid Service Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                deepLink = "paytmedc://blockService?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://blockService";
                deepLink += "&orderId=" + orderId;
                /*if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }*/
                deepLink += "&serviceId=" + serviceId;
                startEDCApp(deepLink);
                break;
            case R.id.btn_update:
                orderId = orderId_update.getText().toString().trim();
                /*if (TextUtils.isEmpty(orderId)) {
                    Toast.makeText(this, "Please enter valid order Id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                String amount = amount_update.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || amount.length() < 3) {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }
                deepLink = "paytmedc://updateCard?" + "callbackAction=" + callBackAction + "&stackClear=true" +
                        "&callbackPkg=com.paytm.sampledeeplinkapp&callbackDl=sampledeeplink://cardUpdate";
                //deepLink += "&orderId=" + orderId;
                deepLink += "&amount=" + amount;
                String cardData = service_update.getText().toString().trim();
                deepLink += "&cardData=" + cardData;
                //deepLink += "&cardNoSHA=" + "64c553a3fa16961c5fa37d4004a032233beb8cca085e2cba56556e5396779c3c";
                //deepLink += "&cardNoSHA=" + "9879bf9751c001b8fba2acc97027ab100abe77fe0ac2dc54b3fd8a5d1de29c97";
                //deepLink += "&cardNoSHA=" + "5f1922264346c999a8c25274d4f89bbf4de596675f90006e822b648db6e7c556c3bd3b187a6593e7d9d4dd478427c15d2c2976959516959b1461902c0f7c1a46";
                //deepLink += "&cardNoSHA=" + "9c420fd342ab7725d42078dc4fa07c194bb2b6c347ef4a8b84b0d206be5b300fde1d4bd1ba0c6c5ff6cca3d66619598e53aafff2f065ca594b6f378bea236f12";
                deepLink += "&featureType=" + 99;
                if(!TextUtils.isEmpty(orderId)){
                    deepLink += "&completeOrderTimeout=" + orderId;
                }
                //deepLink += "&amount=10000&orderId=AT1597145586105&cardNoSHA=380a07f179063dbfda566a66b7f5ce3c53589855e1299a6d4df6b70122d3a88f8bc46c5ea21375795f76a7de981da86cbeffa45dc870e3c8427adc6c6c1cdb92&cardData=310000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
                serviceId = serviceId_service.getText().toString().trim();
                deepLink += "&serviceId=" + serviceId;

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
//        Data.Builder data = new Data.Builder();
//        data.putString("deeplink", deepLink);
//
//        OneTimeWorkRequest workerRequest = new OneTimeWorkRequest.Builder(AppInvokeWorker.class).
//                setInitialDelay(timer, TimeUnit.SECONDS).setInputData(data.build()).build();
//        WorkManager.getInstance(this).enqueue(workerRequest);
//        timer = 0;
    }
}