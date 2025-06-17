package com.paytm.deeplinksample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.paytm.pos.app_invoke_sdk.PaytmPayments;
import com.paytm.printgenerator.Alignment;
import com.paytm.printgenerator.FontSize;
import com.paytm.printgenerator.PageColor;
import com.paytm.printgenerator.Rotation;
import com.paytm.printgenerator.page.GapElement;
import com.paytm.printgenerator.page.ImageElement;
import com.paytm.printgenerator.page.Page;
import com.paytm.printgenerator.page.TextElement;
import com.paytm.printgenerator.printer.PrintStatusCallBack;
import com.paytm.printgenerator.printer.PrintStatusEnum;
import com.paytm.printgenerator.printer.Printer;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.util.Map;

public class DeepLinker extends ReactContextBaseJavaModule implements PaytmPayments.IPaytmResult, ActivityEventListener {
    ReactContext context;

    final String TAG = "DeepLinker";

    public DeepLinker(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "DeepLinker";
    }

    @ReactMethod
    public void startPayment(String amount, String orderId, String payMode) {
        Log.e(TAG, "startPayment() called with: amount = [" + amount + "], orderId = [" + orderId + "], payMode = [" + payMode + "]");
        Toast.makeText(context, "Starting payment", Toast.LENGTH_SHORT).show();
        Activity activity = getReactApplicationContext().getCurrentActivity();
        if (activity == null) {
            // Handle the case where the activity is null (e.g., show an error or return)
            Log.e(TAG, "Current activity is null");
            Toast.makeText(context, "Current activity is null", Toast.LENGTH_SHORT).show();
            return;
        }
        PaytmPayments.INSTANCE.doPayment(activity, this, Integer.parseInt(amount), orderId, payMode, null, null, null, null, null);
        context.addActivityEventListener(this);
    }


    @ReactMethod
    public void printTransactionReceipt(ReadableMap receiptData) {
        if (receiptData == null) return;

        try {
            // Defensive: getString returns null if key not present
            String acquiringBank = receiptData.hasKey("acquiringBank") && !receiptData.isNull("acquiringBank") ? receiptData.getString("acquiringBank") : "";
            String txnId = receiptData.hasKey("txnId") && !receiptData.isNull("txnId") ? receiptData.getString("txnId") : "";
            String cardType = receiptData.hasKey("cardType") && !receiptData.isNull("cardType") ? receiptData.getString("cardType") : "";
            String status = receiptData.hasKey("status") && !receiptData.isNull("status") ? receiptData.getString("status") : "";
            String issuingBank = receiptData.hasKey("issuingBank") && !receiptData.isNull("issuingBank") ? receiptData.getString("issuingBank") : "";
            String responsePayMode = receiptData.hasKey("responsePayMode") && !receiptData.isNull("responsePayMode") ? receiptData.getString("responsePayMode") : "";
            String amount = receiptData.hasKey("amount") && !receiptData.isNull("amount") ? receiptData.getString("amount") : "";

            Page page = new Page();
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_EXTRA_BIG, acquiringBank, null, Alignment.CENTER, 0, 0, 0, 0, 0));
            // Adding empty space
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new GapElement(false, 30, 0, 30));
            // TXN ID, mask if possible
            String maskedTxnId = txnId;
            if (txnId != null && txnId.length() >= 7) {
                maskedTxnId = "***" + txnId.substring(txnId.length() - 7);
            }
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "TXN ID", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, maskedTxnId, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "Card", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, cardType, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "Status", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, status, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "Bank", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, issuingBank, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "Pay Mode", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, responsePayMode, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, "Amount", null, Alignment.LEFT, 0, 1, 0, 0, 0))
                    .addElement(new TextElement(false, false, FontSize.FONT_NORMAL, amount, null, Alignment.RIGHT, 0, 1, 0, 0, 0));
            // Adding image
            page.addLine(0, 0, PageColor.WHITE)
                    .addElement(new ImageElement(getBitmap(R.drawable.ic_business_with_paytm), Rotation.NO_ROTATION, true, PageColor.WHITE, Alignment.CENTER, 0, 0, 0, 0, 0));
            Printer.Companion.print(page, "temp", context, new PrintStatusCallBack() {

                @Override
                public void onPrintStarted(@NonNull String s) {
                    Log.d(TAG, "onPrintStarted , id : " + s);
                }

                @Override
                public void onSuccess(@NonNull String s) {
                    Log.d(TAG, "onSuccess , id : " + s);
                }

                @Override
                public void onFailure(@NonNull String s, @NonNull PrintStatusEnum printStatusEnum) {
                    Log.d(TAG, "onFailure , id : " + s + ", code : " + printStatusEnum.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("DeepLinker", e.getMessage());
        }
    }

    /**
     * @param drawableRes takes drawable
     * @return Bitmap
     */
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * This method is emitting the data from Android side to be received at React side so that we can share data between
     * android and react code.
     */
    private void sendEventToReactFromAndroid(String eventName, @Nullable WritableMap params) {
        context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @Override
    public void onError(int i, @NonNull String s) {
        Toast.makeText(context, "Payment Failed : " + s, Toast.LENGTH_SHORT).show();
        sendEventToReactFromAndroid("OnPaymentResponse", null);
    }

    @Override
    public void onResult(int i, @NonNull Map<String, String> map) {
        WritableMap resultMap = Arguments.createMap();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            resultMap.putString(entry.getKey(), entry.getValue());
        }
        sendEventToReactFromAndroid("OnPaymentResponse", resultMap);
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
