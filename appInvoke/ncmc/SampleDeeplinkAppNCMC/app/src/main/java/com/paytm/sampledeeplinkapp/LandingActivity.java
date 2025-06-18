package com.paytm.sampledeeplinkapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LandingActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        if (getIntent() != null && getIntent().hasExtra("deeplink")) {
            String dl = getIntent().getStringExtra("deeplink");
            response = dl;
            showDialog();
            Uri uri = Uri.parse(dl);

            String amount = uri.getQueryParameter("amount");
            String orderId = uri.getQueryParameter("orderId");
            Log.i("SampleDeeplink", dl);
        }
    }

    public void deepLinkV2(View view) {
        startActivity(new Intent(this, DeepLinkV2Activity.class));
    }

    public void QSPARC(View view) {
        startActivity(new Intent(this, QSPARCActivity.class));
    }

    public void transit(View view) {
        startActivity(new Intent(this, TransitActivity.class));
    }
}
