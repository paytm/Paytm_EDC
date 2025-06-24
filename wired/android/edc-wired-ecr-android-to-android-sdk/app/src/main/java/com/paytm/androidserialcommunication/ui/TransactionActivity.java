package com.paytm.androidserialcommunication.ui;


import static com.paytm.androidserialcommunication.Constants.CANCEL;
import static com.paytm.androidserialcommunication.Constants.CAPTURE;
import static com.paytm.androidserialcommunication.Constants.CONNECTION_CHECK;
import static com.paytm.androidserialcommunication.Constants.PREAUTH;
import static com.paytm.androidserialcommunication.Constants.PRINT;
import static com.paytm.androidserialcommunication.Constants.RELEASE;
import static com.paytm.androidserialcommunication.Constants.SALE;
import static com.paytm.androidserialcommunication.Constants.STATUS_CHECK;
import static com.paytm.androidserialcommunication.Constants.TXN_TYPE;
import static com.paytm.androidserialcommunication.Constants.VOID;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.Group;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.paytm.androidecrsdk.ResponseListener;
import com.paytm.androidecrsdk.constants.ECRCode;
import com.paytm.androidecrsdk.model.Request;
import com.paytm.androidecrsdk.model.Response;
import com.paytm.androidecrsdk.model.SubWalletType;
import com.paytm.androidecrsdk.model.TransactionType;
import com.paytm.androidecrsdk.model.request.CancelRequest;
import com.paytm.androidecrsdk.model.request.ConnectionCheckRequest;
import com.paytm.androidecrsdk.model.request.PrintRequest;
import com.paytm.androidecrsdk.model.request.SaleRequest;
import com.paytm.androidecrsdk.model.request.StatusCheckRequest;
import com.paytm.androidecrsdk.model.request.VoidRequest;
import com.paytm.androidecrsdk.model.response.BaseResponse;
import com.paytm.androidecrsdk.model.response.CancelResponse;
import com.paytm.androidecrsdk.model.response.PrintResponse;
import com.paytm.androidecrsdk.model.response.SaleResponse;
import com.paytm.androidecrsdk.model.response.StatusCheckResponse;
import com.paytm.androidecrsdk.model.response.VoidResponse;
import com.paytm.androidecrsdk.util.MapUtils;
import com.paytm.androidserialcommunication.R;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the Input Activity used to provide input for APIs
 * Please read the wiki for more information regarding the API params.
 * <p>
 * Map Utils can be used to generate formatted strings required in params from maps and vice versa
 *
 * @see MapUtils
 */
public class TransactionActivity extends BaseActivity implements ResponseListener {

    private final String TAG = TransactionActivity.class.getSimpleName();

    private Button mSendRequestBtn;
    private Button mCancelTransaction;
    private String mTxnType;
    private TextInputEditText mMerchantIdET;
    private TextInputEditText mAmountET;

    private TextInputLayout mAmountLT;
    private TextInputEditText mOrderIdET;
    private TextInputEditText mPaymentModeET;
    private TextInputEditText mCardReadModeET;
    private TextInputEditText mSubWalletInfoET;
    private TextInputEditText mGSTInfoET;
    private TextInputEditText mPrintInfoET;
    private TextInputEditText mExtendedInfoET;
    private TextInputLayout mExtendedInfoLT;
    private TextInputLayout mPrintInfoLT;
    private TextInputLayout mPreauthAmountLT;
    private EditText mResponseET;
    private Group mGroup;
    private TextInputLayout mLayoutOrderId, mLayoutMerchantId;
    private TextInputLayout mPreAuthTxnTypeLT;

    private TextInputEditText mPreAuthTxnTypeET;
    private TextInputEditText mTipAmountET;
    private TextInputEditText mTotalAmountET;
    private TextInputEditText mPreauthAmountET;

    private String cancelRequestId = String.valueOf(System.currentTimeMillis());
    private String saleRequestId = String.valueOf(System.currentTimeMillis());
    private String statusCheckRequestId = String.valueOf(System.currentTimeMillis());
    private String connectionCheckRequestId = String.valueOf(System.currentTimeMillis());
    private String voidRequestId = String.valueOf(System.currentTimeMillis());
    private String printRequestId = String.valueOf(System.currentTimeMillis());
    private String preAuthRequestId = String.valueOf(System.currentTimeMillis());
    private String captureRequestId = String.valueOf(System.currentTimeMillis());
    private String releaseRequestId = String.valueOf(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        initComponent();
        Intent intent = getIntent();
        if (intent != null) {
            mTxnType = intent.getStringExtra(TXN_TYPE);
        }

        mSendRequestBtn.setText(mTxnType);

        switch (mTxnType) {

            case STATUS_CHECK: {
                mPreAuthTxnTypeLT.setVisibility(View.GONE);
            }
            case PRINT:
            case CANCEL: {
                mLayoutMerchantId.setVisibility(View.VISIBLE);
                mLayoutOrderId.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.GONE);
                mExtendedInfoLT.setVisibility(View.GONE);
                mPrintInfoLT.setVisibility(View.GONE);
                break;
            }

            case VOID: {
                mLayoutMerchantId.setVisibility(View.VISIBLE);
                mLayoutOrderId.setVisibility(View.VISIBLE);
                mExtendedInfoET.setVisibility(View.VISIBLE);
                mPrintInfoET.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.GONE);
                break;
            }

            case CONNECTION_CHECK: {
                mLayoutMerchantId.setVisibility(View.GONE);
                mLayoutOrderId.setVisibility(View.GONE);
                mGroup.setVisibility(View.GONE);
                mExtendedInfoLT.setVisibility(View.GONE);
                mPrintInfoLT.setVisibility(View.GONE);
                break;
            }

            case CAPTURE:
            case RELEASE: {
                mCancelTransaction.setVisibility(View.VISIBLE);
                mPreauthAmountLT.setVisibility(View.VISIBLE);
                mLayoutMerchantId.setVisibility(View.VISIBLE);
                mLayoutOrderId.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.GONE);
                mExtendedInfoLT.setVisibility(View.GONE);
                mPrintInfoLT.setVisibility(View.GONE);
                break;
            }
            case PREAUTH: {
                mCancelTransaction.setVisibility(View.VISIBLE);
                mLayoutMerchantId.setVisibility(View.VISIBLE);
                mLayoutOrderId.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.VISIBLE);
                mExtendedInfoET.setVisibility(View.VISIBLE);
                mPreauthAmountLT.setVisibility(View.GONE);
                mAmountLT.setVisibility(View.VISIBLE);
                break;
            }


            default: {
                mCancelTransaction.setVisibility(View.VISIBLE);
                mLayoutMerchantId.setVisibility(View.VISIBLE);
                mLayoutOrderId.setVisibility(View.VISIBLE);
                mGroup.setVisibility(View.VISIBLE);
                mExtendedInfoET.setVisibility(View.VISIBLE);
                mAmountLT.setVisibility(View.VISIBLE);
                break;
            }
        }

        mCancelTransaction.setOnClickListener(view -> {
            String merchantId = mMerchantIdET.getEditableText().toString().trim();
            String orderId = mOrderIdET.getEditableText().toString().trim();
            if (validateInput(merchantId, null, orderId)) {
                CancelRequest.Builder builder = new CancelRequest.Builder()
                        .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                        .setOrderId(orderId);
                CancelRequest cancelRequest = builder.build();
                cancelRequestId = String.valueOf(System.currentTimeMillis());
                Request<CancelRequest> request = Request.of(cancelRequest, cancelRequestId);
                payments.doCancel(request, this);
            }
        });

        mSendRequestBtn.setOnClickListener(view -> {

            mSendRequestBtn.setClickable(false);
            mResponseET.setText("");

            String merchantId = mMerchantIdET.getEditableText().toString().trim();
            String amountInPaise = mAmountET.getEditableText().toString().trim();
            String orderId = mOrderIdET.getEditableText().toString().trim();
            String paymentMode = mPaymentModeET.getEditableText().toString().trim();
            String cardReadMode = mCardReadModeET.getEditableText().toString().trim();
            String extendedInfo = mExtendedInfoET.getEditableText().toString().trim();
            String subWalletInfo = mSubWalletInfoET.getEditableText().toString().trim();
            String gstInfo = mGSTInfoET.getEditableText().toString().trim();
            String printInfo = mPrintInfoET.getEditableText().toString().trim();
            String tipAmount = mTipAmountET.getEditableText().toString().trim();
            String totalAmount = mTotalAmountET.getEditableText().toString().trim();
            String preauthAmount = mPreauthAmountET.getEditableText().toString().trim();
            String preAuthTxnType = mPreAuthTxnTypeET.getEditableText().toString().trim();

            switch (mTxnType) {

                case SALE: {

                    if (validateInput(merchantId, amountInPaise, orderId)) {
                        SaleRequest.Builder builder = new SaleRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setAmount(amountInPaise)
                                .setOrderId(orderId)
                                .setPaymentMode(TextUtils.isEmpty(paymentMode) ? "all" : paymentMode)
                                .setCardReadMode(TextUtils.isEmpty(cardReadMode) ? "all" : cardReadMode)
                                .setExtendInfo(TextUtils.isEmpty(extendedInfo) ? null : extendedInfo)
                                .setSubWalletInfo(TextUtils.isEmpty(subWalletInfo) ? null : subWalletInfo)
                                .setGstInformation(TextUtils.isEmpty(gstInfo) ? null : gstInfo)
                                .setPrintInfo(TextUtils.isEmpty(printInfo) ? null : printInfo)
                                .setTipAmount(TextUtils.isEmpty(tipAmount) ? null : tipAmount)
                                .setTotalAmount(TextUtils.isEmpty(totalAmount) ? null : totalAmount);

                        SaleRequest saleRequest = builder.build();
                        saleRequestId = String.valueOf(System.currentTimeMillis());
                        Request<SaleRequest> request = Request.of(saleRequest, saleRequestId);
                        payments.doSale(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }

                case PREAUTH: {

                    if (validateInput(merchantId, amountInPaise, orderId)) {
                        SaleRequest.Builder builder = new SaleRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setAmount(amountInPaise)
                                .setOrderId(orderId)
                                .setPreAuthTxnType(TransactionType.PREAUTH.getValue())
                                .setPaymentMode(TextUtils.isEmpty(paymentMode) ? "all" : paymentMode) // For PreAuth transaction paymentMode should be "Card"
                                .setCardReadMode(TextUtils.isEmpty(cardReadMode) ? "all" : cardReadMode)
                                .setExtendInfo(TextUtils.isEmpty(extendedInfo) ? null : extendedInfo)
                                .setSubWalletInfo(TextUtils.isEmpty(subWalletInfo) ? null : subWalletInfo)
                                .setGstInformation(TextUtils.isEmpty(gstInfo) ? null : gstInfo)
                                .setPrintInfo(TextUtils.isEmpty(printInfo) ? null : printInfo);

                        SaleRequest saleRequest = builder.build();
                        preAuthRequestId = String.valueOf(System.currentTimeMillis());
                        Request<SaleRequest> request = Request.of(saleRequest, preAuthRequestId);
                        payments.doPreAuth(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }

                case CAPTURE: {
                    if (validateInput(merchantId, null, orderId) && isValidPreAuthAmount(preauthAmount)) {
                        SaleRequest.Builder builder = new SaleRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setOrderId(orderId)
                                .setPreAuthTxnType(TransactionType.CAPTURE.getValue())
                                .setPreAuthAmount(preauthAmount);
                        SaleRequest saleRequest = builder.build();
                        captureRequestId = String.valueOf(System.currentTimeMillis());
                        Request<SaleRequest> request = Request.of(saleRequest, captureRequestId);
                        payments.doCapture(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }
                case RELEASE: {
                    if (validateInput(merchantId, null, orderId) && isValidPreAuthAmount(preauthAmount)) {
                        SaleRequest.Builder builder = new SaleRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setOrderId(orderId)
                                .setPreAuthTxnType(TransactionType.RELEASE.getValue())
                                .setPreAuthAmount(preauthAmount);
                        SaleRequest saleRequest = builder.build();
                        releaseRequestId = String.valueOf(System.currentTimeMillis());
                        Request<SaleRequest> request = Request.of(saleRequest, releaseRequestId);
                        payments.doRelease(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }

                case STATUS_CHECK: {
                    if (validateInput(merchantId, null, orderId)) {
                        StatusCheckRequest.Builder builder = new StatusCheckRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setPreAuthTxnType(TextUtils.isEmpty(preAuthTxnType) ? null : preAuthTxnType)
                                .setOrderId(orderId);

                        StatusCheckRequest statusCheckRequest = builder.build();
                        statusCheckRequestId = String.valueOf(System.currentTimeMillis());
                        Request<StatusCheckRequest> request = Request.of(statusCheckRequest, statusCheckRequestId);
                        payments.doStatusCheck(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }

                case CANCEL: {
                    if (validateInput(merchantId, null, orderId)) {
                        CancelRequest.Builder builder = new CancelRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setOrderId(orderId);
                        CancelRequest cancelRequest = builder.build();
                        cancelRequestId = String.valueOf(System.currentTimeMillis());
                        Request<CancelRequest> request = Request.of(cancelRequest, cancelRequestId);
                        payments.doCancel(request, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                }

                case CONNECTION_CHECK:
                    ConnectionCheckRequest connectionCheckRequest = new ConnectionCheckRequest.Builder().build();
                    connectionCheckRequestId = String.valueOf(System.currentTimeMillis());
                    Request<ConnectionCheckRequest> request = Request.of(connectionCheckRequest, connectionCheckRequestId);
                    payments.doConnectionCheck(request, this);
                    mSendRequestBtn.setClickable(true);
                    break;
                case VOID:
                    if (validateInput(merchantId, null, orderId)) {
                        VoidRequest.Builder builder = new VoidRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setOrderId(orderId)
                                .setExtendInfo(TextUtils.isEmpty(extendedInfo) ? null : extendedInfo)
                                .setPrintInfo(TextUtils.isEmpty(printInfo) ? null : printInfo);
                        VoidRequest voidRequest = builder.build();
                        voidRequestId = String.valueOf(System.currentTimeMillis());
                        Request<VoidRequest> request2 = Request.of(voidRequest, voidRequestId);
                        payments.doVoid(request2, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;
                case PRINT:
                    if (validateInput(merchantId, null, orderId)) {
                        PrintRequest.Builder builder = new PrintRequest.Builder()
                                .setMerchantId(TextUtils.isEmpty(merchantId) ? null : merchantId)
                                .setOrderId(orderId);
                        PrintRequest printRequest = builder.build();
                        printRequestId = String.valueOf(System.currentTimeMillis());
                        Request<PrintRequest> request3 = Request.of(printRequest, printRequestId);
                        payments.doPrint(request3, this);
                    } else {
                        mSendRequestBtn.setClickable(true);
                    }
                    break;

                default:
                    break;
            }
        });

    }

    private boolean validateInput(String merchantId, String amountInPaise, String orderId) {
        boolean isValid = true;
        if (merchantId != null && !merchantId.matches("[a-zA-Z0-9]*")) {
            mMerchantIdET.setError("Special Characters are not allowed here.");
            isValid = false;
        } else {
            mMerchantIdET.setError(null);
        }
        if (amountInPaise != null && (TextUtils.isEmpty(amountInPaise) || amountInPaise.length() < 3 || amountInPaise.length() > 11)) {
            mAmountET.setError("Please enter a valid amount.");
            isValid = false;
        } else {
            mAmountET.setError(null);
        }
        if (orderId != null && (TextUtils.isEmpty(orderId) || !orderId.matches("[a-zA-Z0-9]*"))) {
            mOrderIdET.setError("Please enter a valid order id.");
            isValid = false;
        } else {
            mOrderIdET.setError(null);
        }
        return isValid;
    }

    private boolean isValidPreAuthAmount(String amountInPaise) {
        if (amountInPaise != null && (TextUtils.isEmpty(amountInPaise) || amountInPaise.length() < 3 || amountInPaise.length() > 11)) {
            mPreauthAmountET.setError("Please enter a valid amount.");
            return false;
        } else {
            mPreauthAmountET.setError(null);
        }
        return true;
    }

    private void initComponent() {
        mSendRequestBtn = findViewById(R.id.btn_sendrequest);
        mCancelTransaction = findViewById(R.id.btn_canceltransaction);
        mMerchantIdET = findViewById(R.id.et_merchantId);
        mAmountET = findViewById(R.id.et_amount);
        mAmountLT = findViewById(R.id.layout_amount);
        mOrderIdET = findViewById(R.id.et_orderid);
        mPaymentModeET = findViewById(R.id.et_paymentMode);
        mCardReadModeET = findViewById(R.id.et_cardReadMode);
        mExtendedInfoET = findViewById(R.id.et_extended_info);
        mExtendedInfoLT = findViewById(R.id.layout_extended_info);
        mPrintInfoLT = findViewById(R.id.layout_print_info);
        mGSTInfoET = findViewById(R.id.et_gst_info);
        mSubWalletInfoET = findViewById(R.id.et_sub_wallet_info);
        mPrintInfoET = findViewById(R.id.et_print_info);
        mResponseET = findViewById(R.id.et_response);
        mGroup = findViewById(R.id.group);
        mLayoutOrderId = findViewById(R.id.layout_orderid);
        mLayoutMerchantId = findViewById(R.id.layout_merchantId);
        mTipAmountET = findViewById(R.id.et_tipAmount);
        mTotalAmountET = findViewById(R.id.et_totalAmount);
        mPreauthAmountLT = findViewById(R.id.layout_preauth_amount);
        mPreauthAmountET = findViewById(R.id.et_preauth_amount);
        mPreAuthTxnTypeLT = findViewById(R.id.layout_preauth_txn_type);
        mPreAuthTxnTypeET = findViewById(R.id.et_preauth_txn_type);
    }

    @Override
    public void onResponse(Response<? extends BaseResponse> response) {
        handleResponse(response);
    }

    private void handleSaleResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Sale request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else if (response.getData() instanceof SaleResponse) { // if statusCheckOnSaleRequestEnabled is false

            SaleResponse saleResponse = (SaleResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_INITIATED.getCode().equals(saleResponse.getStatusCode())) {
                // Sale transaction was successfully initiated on the device
                Log.d(TAG, String.format("Sale Transaction with orderId : %s was successfully initiated on the device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.DUPLICATE_TRANSACTION_REQUEST.getCode().equals(saleResponse.getStatusCode())) {
                // Sale transaction could not be initiated because of duplicate orderId provided
                Log.d(TAG, String.format("Sale Transaction with orderId : %s could not be initiated because of duplicate orderId provided",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INTERNET_NOT_CONNECTED.getCode().equals(saleResponse.getStatusCode())) {
                // Sale transaction could not be initiated because of no network connection on the device
                Log.d(TAG, String.format("Sale Transaction with orderId : %s could not be initiated because of no network connection on device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INVALID_REQUEST_PARAM.getCode().equals(saleResponse.getStatusCode()) ||
                    ECRCode.INVALID_COMMAND.getCode().equals(saleResponse.getStatusCode())) {
                // Sale transaction could not be initiated because of invalid param(s) in Sale request
                Log.d(TAG, String.format("Sale Transaction with orderId : %s could not be initiated because of invalid param(s) in Sale request",
                        saleResponse.getOrderId()));
            } else if (ECRCode.TERMINAL_IS_BUSY.getCode().equals(saleResponse.getStatusCode())) {
                // Sale transaction could not be initiated because terminal is busy
                Log.d(TAG, String.format("Sale Transaction with orderId : %s could not be initiated because terminal is busy",
                        saleResponse.getOrderId()));
            } else {
                // Sale transaction could not be initiated on the device, check status code
                Log.d(TAG, String.format("Sale Transaction with orderId : %s could not be initiated by device, error returned : %s",
                        saleResponse.getOrderId(),
                        saleResponse.getStatusMessage()));
            }
        } else if (response.getData() instanceof StatusCheckResponse) { // If statusCheckOnSaleRequestEnabled flag is true in Config

            StatusCheckResponse statusCheckResponse = (StatusCheckResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was successful
                Log.d(TAG, String.format("Sale transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was cancelled
                Log.d(TAG, String.format("Sale transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was failure
                Log.d(TAG, String.format("Sale transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            }
        }
    }

    private void handlePreAuthResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in PreAuth request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else if (response.getData() instanceof SaleResponse) { // if statusCheckOnSaleRequestEnabled is false

            SaleResponse saleResponse = (SaleResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_INITIATED.getCode().equals(saleResponse.getStatusCode())) {
                // Pre-Auth transaction was successfully initiated on the device
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s was successfully initiated on the device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.DUPLICATE_TRANSACTION_REQUEST.getCode().equals(saleResponse.getStatusCode())) {
                // Pre-Auth transaction could not be initiated because of duplicate orderId provided
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s could not be initiated because of duplicate orderId provided",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INTERNET_NOT_CONNECTED.getCode().equals(saleResponse.getStatusCode())) {
                // Pre-Auth transaction could not be initiated because of no network connection on the device
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s could not be initiated because of no network connection on device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INVALID_REQUEST_PARAM.getCode().equals(saleResponse.getStatusCode()) ||
                    ECRCode.INVALID_COMMAND.getCode().equals(saleResponse.getStatusCode())) {
                // Pre-Auth transaction could not be initiated because of invalid param(s) in Sale request
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s could not be initiated because of invalid param(s) in Sale request",
                        saleResponse.getOrderId()));
            } else if (ECRCode.TERMINAL_IS_BUSY.getCode().equals(saleResponse.getStatusCode())) {
                // Pre-Auth transaction could not be initiated because terminal is busy
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s could not be initiated because terminal is busy",
                        saleResponse.getOrderId()));
            } else {
                // Pre-Auth transaction could not be initiated on the device, check status code
                Log.d(TAG, String.format("Pre-Auth Transaction with orderId : %s could not be initiated by device, error returned : %s",
                        saleResponse.getOrderId(),
                        saleResponse.getStatusMessage()));
            }
        } else if (response.getData() instanceof StatusCheckResponse) { // If statusCheckOnSaleRequestEnabled flag is true in Config

            StatusCheckResponse statusCheckResponse = (StatusCheckResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Pre-Auth Transaction was successful
                Log.d(TAG, String.format("Pre-Auth transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Pre-Auth Transaction was cancelled
                Log.d(TAG, String.format("Pre-Auth transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Pre-Auth Transaction was failure
                Log.d(TAG, String.format("Pre-Auth transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            }
        }
    }

    private void handleCaptureResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Capture request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else if (response.getData() instanceof SaleResponse) { // if statusCheckOnSaleRequestEnabled is false

            SaleResponse saleResponse = (SaleResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_INITIATED.getCode().equals(saleResponse.getStatusCode())) {
                // Capture transaction was successfully initiated on the device
                Log.d(TAG, String.format("Capture Transaction with orderId : %s was successfully initiated on the device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INTERNET_NOT_CONNECTED.getCode().equals(saleResponse.getStatusCode())) {
                // Capture transaction could not be initiated because of no network connection on the device
                Log.d(TAG, String.format("Capture Transaction with orderId : %s could not be initiated because of no network connection on device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INVALID_REQUEST_PARAM.getCode().equals(saleResponse.getStatusCode()) ||
                    ECRCode.INVALID_COMMAND.getCode().equals(saleResponse.getStatusCode())) {
                // Capture transaction could not be initiated because of invalid param(s) in Sale request
                Log.d(TAG, String.format("Capture Transaction with orderId : %s could not be initiated because of invalid param(s) in Sale request",
                        saleResponse.getOrderId()));
            } else if (ECRCode.TERMINAL_IS_BUSY.getCode().equals(saleResponse.getStatusCode())) {
                // Capture transaction could not be initiated because terminal is busy
                Log.d(TAG, String.format("Capture Transaction with orderId : %s could not be initiated because terminal is busy",
                        saleResponse.getOrderId()));
            } else {
                // Capture transaction could not be initiated on the device, check status code
                Log.d(TAG, String.format("Capture Transaction with orderId : %s could not be initiated by device, error returned : %s",
                        saleResponse.getOrderId(),
                        saleResponse.getStatusMessage()));
            }
        } else if (response.getData() instanceof StatusCheckResponse) { // If statusCheckOnSaleRequestEnabled flag is true in Config

            StatusCheckResponse statusCheckResponse = (StatusCheckResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Capture Transaction was successful
                Log.d(TAG, String.format("Capture transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Capture Transaction was cancelled
                Log.d(TAG, String.format("Capture transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Capture Transaction was failure
                Log.d(TAG, String.format("Capture transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_PENDING.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Capture Transaction is pending
                Log.d(TAG, String.format("Capture transaction with orderId : %s is in pending state",
                        statusCheckResponse.getOrderId()));
            }
        }
    }

    private void handleReleaseResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Release request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else if (response.getData() instanceof SaleResponse) { // if statusCheckOnSaleRequestEnabled is false

            SaleResponse saleResponse = (SaleResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_INITIATED.getCode().equals(saleResponse.getStatusCode())) {
                // Release transaction was successfully initiated on the device
                Log.d(TAG, String.format("Release Transaction with orderId : %s was successfully initiated on the device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INTERNET_NOT_CONNECTED.getCode().equals(saleResponse.getStatusCode())) {
                // Release transaction could not be initiated because of no network connection on the device
                Log.d(TAG, String.format("Release Transaction with orderId : %s could not be initiated because of no network connection on device",
                        saleResponse.getOrderId()));
            } else if (ECRCode.INVALID_REQUEST_PARAM.getCode().equals(saleResponse.getStatusCode()) ||
                    ECRCode.INVALID_COMMAND.getCode().equals(saleResponse.getStatusCode())) {
                // Release transaction could not be initiated because of invalid param(s) in Sale request
                Log.d(TAG, String.format("Release Transaction with orderId : %s could not be initiated because of invalid param(s) in Sale request",
                        saleResponse.getOrderId()));
            } else if (ECRCode.TERMINAL_IS_BUSY.getCode().equals(saleResponse.getStatusCode())) {
                // Release transaction could not be initiated because terminal is busy
                Log.d(TAG, String.format("Release Transaction with orderId : %s could not be initiated because terminal is busy",
                        saleResponse.getOrderId()));
            } else {
                // Release transaction could not be initiated on the device, check status code
                Log.d(TAG, String.format("Release Transaction with orderId : %s could not be initiated by device, error returned : %s",
                        saleResponse.getOrderId(),
                        saleResponse.getStatusMessage()));
            }
        } else if (response.getData() instanceof StatusCheckResponse) { // If statusCheckOnSaleRequestEnabled flag is true in Config

            StatusCheckResponse statusCheckResponse = (StatusCheckResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Release Transaction was successful
                Log.d(TAG, String.format("Release transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Release Transaction was cancelled
                Log.d(TAG, String.format("Release transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Release Transaction was failure
                Log.d(TAG, String.format("Release transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_PENDING.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Release Transaction is pending
                Log.d(TAG, String.format("Release transaction with orderId : %s is in pending state",
                        statusCheckResponse.getOrderId()));
            }
        }
    }

    private void handleStatusCheckResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Status Check request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else {

            StatusCheckResponse statusCheckResponse = (StatusCheckResponse) response.getData();

            if (ECRCode.PROCESS_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was successful
                Log.d(TAG, String.format("Sale Transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was cancelled
                Log.d(TAG, String.format("Sale Transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.PROCESS_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Sale Transaction was failure
                Log.d(TAG, String.format("Sale Transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.VOID_TRANSACTION_SUCCESS.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Void for transaction was successful
                Log.d(TAG, String.format("Void for transaction with orderId : %s was successful",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.VOID_TRANSACTION_CANCELLED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Void for transaction was cancelled
                Log.d(TAG, String.format("Void for transaction with orderId : %s was cancelled",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.VOID_TRANSACTION_FAILED.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Void for transaction was failure
                Log.d(TAG, String.format("Void for transaction with orderId : %s was failure",
                        statusCheckResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_DOESNT_EXIST.getCode().equals(statusCheckResponse.getStatusCode())) {
                // Transaction with given orderId does not exists in device DB
                Log.d(TAG, String.format("Transaction with orderId : %s , does not exists in device DB",
                        statusCheckResponse.getOrderId()));
            }
        }
    }

    private void handleCancelResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Cancel request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else {
            CancelResponse cancelResponse = (CancelResponse) response.getData();
            if (ECRCode.TRANSACTION_CANCEL_SUCCESS.getCode().equals(cancelResponse.getStatusCode())) {
                // Sale transaction was cancelled successfully
                Log.d(TAG, String.format("Sale transaction with orderId : %s was cancelled successfully",
                        cancelResponse.getOrderId()));
            } else if (ECRCode.VOID_CANCEL_SUCCESS.getCode().equals(cancelResponse.getStatusCode())) {
                // Void for transaction was cancelled successfully
                Log.d(TAG, String.format("Void for transaction with orderId : %s was cancelled successfully",
                        cancelResponse.getOrderId()));
            } else if (ECRCode.NO_CANCELLABLE_TRANSACTION.getCode().equals(cancelResponse.getStatusCode())) {
                // No Sale or Void request with given orderId was being executed
                Log.d(TAG, String.format("No Sale or Void request with orderId : %s was being executed",
                        cancelResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_CANCEL_FAILURE.getCode().equals(cancelResponse.getStatusCode())) {
                // Sale transaction could not be cancelled
                Log.d(TAG, String.format("Sale transaction with orderId : %s could not be cancelled",
                        cancelResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_CANCEL_FAILURE_CARD_SCANNED.getCode().equals(cancelResponse.getStatusCode())) {
                // Sale transaction could not be cancelled, because card data was already read
                Log.d(TAG, String.format("Sale transaction with orderId : %s could not be cancelled, because card data was already read",
                        cancelResponse.getOrderId()));
            } else if (ECRCode.VOID_CANCEL_FAILURE.getCode().equals(cancelResponse.getStatusCode())) {
                // Void for transaction could not be cancelled
                Log.d(TAG, String.format("Void for transaction with orderId : %s could not be cancelled",
                        cancelResponse.getOrderId()));
            }
        }
    }

    private void handleConnectionCheckResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Connection Check request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else {
            Log.d(TAG, "EDC device is connected and ready to communicate");
        }
    }

    private void handleVoidResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Void request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else {

            VoidResponse voidResponse = (VoidResponse) response.getData();

            if (ECRCode.VOID_TRANSACTION_INITIATED.getCode().equals(voidResponse.getStatusCode())) {
                // Void for transaction was successfully initiated on the device
                Log.d(TAG, String.format("Void for transaction with orderId : %s initiated by device ",
                        voidResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_NOT_SUCCESSFUL.getCode().equals(voidResponse.getStatusCode())) {
                // Void could not be done, because transaction was not successful
                Log.d(TAG, String.format("Void for transaction with orderId : %s could not be done, because transaction was not successful",
                        voidResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_DOESNT_EXIST.getCode().equals(voidResponse.getStatusCode())) {
                // Void could not be done, because transaction with given orderId does not exists in device DB
                Log.d(TAG, String.format("Void for transaction with orderId : %s could not be done, because transaction with given orderId does not exists in device DB",
                        voidResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_ALREADY_VOID.getCode().equals(voidResponse.getStatusCode())) {
                // Void could not be done, because void for the transaction was already successful
                Log.d(TAG, String.format("Void for transaction with orderId : %s could not be done, because void for the transaction was already successful",
                        voidResponse.getOrderId()));
            }

        }
    }

    private void handlePrintResponse(Response<? extends BaseResponse> response) {
        if (response.getStatus() == Response.Status.ERROR) {
            // Check errorType and take action
            Log.d(TAG, String.format("Error occurred in Print request with requestId : %s, error : %s",
                    response.getRequestId(),
                    response.getError().getMsg()));
        } else {
            PrintResponse printResponse = (PrintResponse) response.getData();
            if (ECRCode.COMMAND_EXECUTION_SUCCESSFUL.getCode().equals(printResponse.getStatusCode())) {
                // Print command was successful
                Log.d(TAG, String.format("Successfully printed receipt for transaction with orderId : %s",
                        printResponse.getOrderId()));
            } else if (ECRCode.TRANSACTION_DOESNT_EXIST.getCode().equals(printResponse.getStatusCode())) {
                // Print receipt could not be generated, because transaction with given orderId does not exists in device DB
                Log.d(TAG, String.format("Print receipt could not be generated for transaction with orderId : %s , because transaction with given orderId does not exists in device DB",
                        printResponse.getOrderId()));
            } else {
                // command was not successful , check status code
                Log.d(TAG, String.format("Could not print receipt for orderId : %s, error returned : %s",
                        printResponse.getOrderId(),
                        printResponse.getStatusMessage()));
            }
        }
    }

    private void handleResponse(Response<? extends BaseResponse> response) {
        runOnUiThread(() -> {
            mSendRequestBtn.setClickable(true);
            if (response.getStatus() == Response.Status.ERROR) {
                Toast.makeText(this, response.getError().getMsg(), Toast.LENGTH_SHORT).show();
                mResponseET.setText(response.toString());
            } else {
                mResponseET.setText(response.getData().toString());
            }

            // These method calls are for demonstration purpose on different expected cases
            if (response.getRequestId().equals(saleRequestId)) {
                handleSaleResponse(response);
            } else if (response.getRequestId().equals(statusCheckRequestId)) {
                handleStatusCheckResponse(response);
            } else if (response.getRequestId().equals(cancelRequestId)) {
                handleCancelResponse(response);
            } else if (response.getRequestId().equals(connectionCheckRequestId)) {
                handleConnectionCheckResponse(response);
            } else if (response.getRequestId().equals(printRequestId)) {
                handlePrintResponse(response);
            } else if (response.getRequestId().equals(voidRequestId)) {
                handleVoidResponse(response);
            } else if (response.getRequestId().equals(preAuthRequestId)) {
                handlePreAuthResponse(response);
            } else if (response.getRequestId().equals(captureRequestId)) {
                handleCaptureResponse(response);
            } else if (response.getRequestId().equals(releaseRequestId)) {
                handleReleaseResponse(response);
            }
        });
    }

    private String getExtendInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("extendKey1", "extendVal1");
        map.put("extendKey2", "extendVal2");
        map.put("extendKey3", "extendVal3");
        return MapUtils.getExtendInfoString(map);
    }

    private String getPrintInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("printKey1", "printVal1");
        map.put("printKey2", "printVal2");
        map.put("printKey3", "printVal3");
        return MapUtils.getPrintInfoString(map);
    }

    private String getSubWalletInfo() {
        Map<String, String> map = new HashMap<>();
        map.put(SubWalletType.FOOD.getVal(), "100");
        map.put(SubWalletType.GIFT.getVal(), "100");
        return MapUtils.getSeparatedStringFromMap(map);
    }

    private String getGstInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("gstIn", "08TESTF0078P1ZP");
        map.put("gstBrkUp", MapUtils.getGstBreakupString("10", "10", "10", "10", "10", "10"));
        map.put("invoiceNo", "Invoice34234321");
        map.put("invoiceDate", "20210512142919");
        return MapUtils.getGstInfoString(map);
    }
}