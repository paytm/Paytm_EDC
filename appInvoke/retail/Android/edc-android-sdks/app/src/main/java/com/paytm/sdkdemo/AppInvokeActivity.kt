package com.paytm.sdkdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import com.paytm.pos.app_invoke_sdk.PaytmPayments

class AppInvokeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etAmount: EditText
    private lateinit var etOrderId: EditText
    private lateinit var etManufactureType: EditText
    private lateinit var etCardBlockNumber: EditText
    private lateinit var etCardData: EditText
    private lateinit var payModeGroup: RadioGroup
    private lateinit var mapGroup: RadioGroup
    private lateinit var etKey: EditText
    private lateinit var etValue: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button
    private lateinit var mapGst: RadioGroup
    private lateinit var etKeyGst: EditText
    private lateinit var etValueGst: EditText
    private lateinit var btnAddGst: Button
    private lateinit var btnRemoveGst: Button
    private lateinit var btnPay: Button
    private lateinit var btnStatus: Button
    private lateinit var btnVoid: Button
    private lateinit var btnReadCard: Button
    private lateinit var btnUpdateCard: Button
    private lateinit var tvRequest: TextView
    private lateinit var tvResponse: TextView
    private lateinit var btnReset: Button


    private val payment = R.id.btn_payment
    private val status = R.id.btn_status
    private val void = R.id.btn_void
    private val readCard = R.id.btn_readCard
    private val updateCard = R.id.btn_updateCard
    private val add = R.id.add
    private val remove = R.id.remove
    private val addGst = R.id.addGst
    private val removeGst = R.id.removeGst
    private val reset = R.id.reset

    private var selectedPaymode = "ALL"
    private var selectedMap = "Extend"
    private var selectedGst = "gstIn"

    private val extendInfoMap = HashMap<String, String>()
    private val printInfoMap = HashMap<String, String>()
    private val optionalParamMap = HashMap<String, String>()
    private val subWalletInfoMap = HashMap<String, String>()
    private val gstBrkUp = HashMap<String, String>()
    private var gstIn: String? = null
    private var gstInvoiceDate: String? = null
    private var gstInvoiceNo: String? = null

    private val paytmResponseListener = object : PaytmPayments.IPaytmResult {
        override fun onResult(requestId: Int, responseData: Map<String, String>) {
            val message = "RESPONSE\n" +
                    "onResult -> \n" +
                    "Code : $requestId \n" +
                    "Data : $responseData"
            tvResponse.text = message
        }

        override fun onError(requestId: Int, errorMsg: String) {
            val message = "RESPONSE\n" +
                    "onError -> \n$requestId : $errorMsg"
            tvResponse.text = message
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_invoke)
        initViewProperties()
        setClickListeners()
        refreshRequestTv(0, "")
    }

    private fun initViewProperties() {
        etAmount = findViewById(R.id.amount)
        etOrderId = findViewById(R.id.orderId)
        etManufactureType = findViewById(R.id.manufactureType)
        etCardBlockNumber = findViewById(R.id.cardBlockNumber)
        etCardData = findViewById(R.id.cardData)
        payModeGroup = findViewById(R.id.rdPaymode)
        mapGroup = findViewById(R.id.rdMap)
        etKey = findViewById(R.id.key)
        etValue = findViewById(R.id.value)
        btnAdd = findViewById(add)
        btnRemove = findViewById(remove)
        mapGst = findViewById(R.id.rdGst)
        etKeyGst = findViewById(R.id.keyGst)
        etValueGst = findViewById(R.id.valueGst)
        btnAddGst = findViewById(addGst)
        btnRemoveGst = findViewById(removeGst)
        btnPay = findViewById(payment)
        btnStatus = findViewById(status)
        btnVoid = findViewById(void)
        btnReadCard = findViewById(readCard)
        tvResponse = findViewById(R.id.tv_response)
        tvRequest = findViewById(R.id.tv_request)
        btnReset = findViewById(reset)
        btnUpdateCard = findViewById(updateCard)
    }

    private fun initiatePayment(amountInPaise: Int, orderId: String) {
        val gstInfo = if (gstIn != null && gstInvoiceNo != null && gstInvoiceDate != null)
            PaytmPayments.GstInfo(
                gstIn ?: "",
                gstInvoiceNo ?: "",
                gstInvoiceDate ?: "",
                gstBrkUp
            ) else null
        PaytmPayments.doPayment(
            activity = this,
            paytmResult = paytmResponseListener,
            amountInPaise = amountInPaise,
            orderId = orderId,
            payMode = selectedPaymode, //Optional
            subWalletInfo = subWalletInfoMap, //Optional
            printInfo = printInfoMap, //Optional
            extendInfo = extendInfoMap, //Optional
            optionalParams = optionalParamMap, //Optional,
            gstInfo = gstInfo //Optional
        )
    }

    private fun initiatePaymentAfterStatusCheck(amountInPaise: Int, orderId: String) {
        val gstInfo = if (gstIn != null && gstInvoiceNo != null && gstInvoiceDate != null)
            PaytmPayments.GstInfo(
                gstIn ?: "",
                gstInvoiceNo ?: "",
                gstInvoiceDate ?: "",
                gstBrkUp
            ) else null
        PaytmPayments.checkStatus(
            activity = this,
            paytmResult = object : PaytmPayments.IPaytmResult {
                override fun onResult(requestId: Int, responseData: Map<String, String>) {
                    if (responseData["STATUS"] != "SUCCESS") {
                        PaytmPayments.doPayment(
                            activity = this@AppInvokeActivity,
                            paytmResult = paytmResponseListener,
                            amountInPaise = amountInPaise,
                            orderId = orderId,
                            payMode = selectedPaymode, //Optional
                            subWalletInfo = subWalletInfoMap, //Optional
                            printInfo = printInfoMap, //Optional
                            extendInfo = extendInfoMap, //Optional
                            optionalParams = optionalParamMap, //Optional,
                            gstInfo = gstInfo //Optional
                        )
                    } else {
                        Toast.makeText(
                            this@AppInvokeActivity,
                            "Txn was already success",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onError(requestId: Int, errorMsg: String) {
                    Toast.makeText(this@AppInvokeActivity, errorMsg, Toast.LENGTH_SHORT).show()
                }
            },
            orderId = orderId
        )
    }

    private fun checkStatus(orderId: String) {
        PaytmPayments.checkStatus(
            activity = this,
            paytmResult = paytmResponseListener,
            orderId = orderId,
            optionalParams = optionalParamMap, //Optional
        )
    }

    private fun initiateVoid(orderId: String) {
        PaytmPayments.doVoid(
            activity = this,
            paytmResult = paytmResponseListener,
            orderId = orderId,
            optionalParams = optionalParamMap, //Optional
        )
    }

    private fun readCard(manufactureType: String, cardBlockNumber: String? = null, orderId: String) {
        PaytmPayments.readCloseLoopCard(
            activity = this,
            paytmResult = paytmResponseListener,
            orderId = orderId,
            optionalParams = optionalParamMap, //Optional
            manufactureType = manufactureType,
            cardBlockNumber = cardBlockNumber
        )
    }

    private fun updateCard(
        manufactureType: String,
        cardData: String,
        cardBlockNumber: String,
        orderId: String
    ) {
        PaytmPayments.updateCloseLoopCard(
            activity = this,
            paytmResult = paytmResponseListener,
            orderId = orderId,
            optionalParams = optionalParamMap, //Optional
            manufactureType = manufactureType,
            cardData = cardData,
            cardBlockNumber = cardBlockNumber
        )
    }

    private fun setClickListeners() {
        btnPay.setOnClickListener(this)
        btnStatus.setOnClickListener(this)
        btnVoid.setOnClickListener(this)
        btnReadCard.setOnClickListener(this)
        btnAdd.setOnClickListener(this)
        btnRemove.setOnClickListener(this)
        btnAddGst.setOnClickListener(this)
        btnRemoveGst.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnUpdateCard.setOnClickListener(this)

        payModeGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedPaymode = findViewById<RadioButton>(checkedId).text.toString()
        }

        mapGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedMap = findViewById<RadioButton>(checkedId).text.toString()
        }

        mapGst.setOnCheckedChangeListener { _, checkedId ->
            selectedGst = findViewById<RadioButton>(checkedId).text.toString()
        }
    }

    override fun onClick(v: View?) {
        val amountString = etAmount.text.toString()
        val amount = if (amountString.isNotEmpty()) Integer.valueOf(etAmount.text.toString()) else 0
        val orderId = etOrderId.text.toString()
        val manufactureType = etManufactureType.text.toString()
        val cardBlockNumber = etCardBlockNumber.text.toString()
        val cardData = etCardData.text.toString()
        val key = etKey.text.toString()
        val value = etValue.text.toString()
        val keyGst = etKeyGst.text.toString()
        val valueGst = etValueGst.text.toString()

        when (v?.id) {
            payment -> {
//                initiatePayment(amount, orderId)
                initiatePaymentAfterStatusCheck(amount, orderId)
            }

            status -> {
                checkStatus(orderId)
            }

            void -> {
                initiateVoid(orderId)
            }

            readCard -> {
                readCard(manufactureType, cardBlockNumber, orderId)
            }

            add -> {
                addParamToMap(key, value)
            }

            remove -> {
                removeParamFromMap(key)
            }

            addGst -> {
                addParamToGst(keyGst, valueGst)
            }

            removeGst -> {
                removeParamFromGst(keyGst)
            }

            reset -> {
                clearForm()
            }

            updateCard -> {
                updateCard(manufactureType, cardData, cardBlockNumber, orderId)
            }
        }
        refreshRequestTv(amount, orderId)
    }

    private fun clearForm() {
        etAmount.text = null
        etOrderId.text = null
        etManufactureType.text = null
        etCardBlockNumber.text = null
        etCardData.text = null
        etKey.text = null
        etValue.text = null
        extendInfoMap.clear()
        printInfoMap.clear()
        optionalParamMap.clear()
        subWalletInfoMap.clear()
        refreshRequestTv(0, "")
    }

    private fun refreshRequestTv(amountInPaise: Int, orderId: String) {
        val gstInfo = PaytmPayments.GstInfo(
            gstIn ?: "",
            gstInvoiceNo ?: "",
            gstInvoiceDate ?: "",
            gstBrkUp
        )
        val gstValid =
            if (gstIn != null && gstInvoiceNo != null && gstInvoiceDate != null) "(Used)" else "(Unused)"
        val request = "REQUEST\n" +
                "Amount : $amountInPaise \n" +
                "Order ID : $orderId \n" +
                "PayMode : $selectedPaymode \n" +
                "ExtendInfo : $extendInfoMap \n" +
                "PrintInfo : $printInfoMap \n" +
                "OptionalParams : $optionalParamMap \n" +
                "SubWalletInfo : $subWalletInfoMap \n" +
                "GstInfo $gstValid : $gstInfo\n"+
                "ManufactureType ${etManufactureType.text}\n"+
                "cardBlockNumber ${etCardBlockNumber.text}\n"+
                "cardData ${etCardData.text}\n"

        tvRequest.text = request
    }

    private fun addParamToMap(key: String, value: String) {
        if (key.isEmpty() || value.isEmpty()) return
        when (selectedMap) {
            "Extend" -> {
                extendInfoMap[key] = value
                Toast.makeText(this, "$key added in  Extend Map", Toast.LENGTH_SHORT).show()
            }

            "Print" -> {
                printInfoMap[key] = value
                Toast.makeText(this, "$key added in  Print Map", Toast.LENGTH_SHORT).show()
            }

            "Optional" -> {
                optionalParamMap[key] = value
                Toast.makeText(this, "$key added in  Optional Map", Toast.LENGTH_SHORT).show()
            }

            "SubWall" -> {
                subWalletInfoMap[key] = value
                Toast.makeText(this, "$key added in  SubWall Map", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun removeParamFromMap(key: String) {
        if (key.isEmpty()) return
        when (selectedMap) {
            "Extend" -> {
                extendInfoMap.remove(key)
                Toast.makeText(this, "$key removed from Extend Map", Toast.LENGTH_SHORT).show()
            }

            "Print" -> {
                printInfoMap.remove(key)
                Toast.makeText(this, "$key removed from Print Map", Toast.LENGTH_SHORT).show()
            }

            "Optional" -> {
                optionalParamMap.remove(key)
                Toast.makeText(this, "$key removed from Optional Map", Toast.LENGTH_SHORT).show()
            }

            "SubWall" -> {
                subWalletInfoMap.remove(key)
                Toast.makeText(this, "$key removed from SubWall Map", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addParamToGst(key: String, value: String) {
        if (value.isEmpty()) return
        when (selectedGst) {
            "gstIn" -> {
                gstIn = value
            }

            "gstBrkUp" -> {
                gstBrkUp[key] = value
            }

            "gstInvoiceNo" -> {
                gstInvoiceNo = value
            }

            "gstInvoiceDate" -> {
                gstInvoiceDate = value
            }
        }
    }

    private fun removeParamFromGst(key: String) {
        when (selectedGst) {
            "gstIn" -> {
                gstIn = null
            }

            "gstBrkUp" -> {
                gstBrkUp.remove(key)
            }

            "gstInvoiceNo" -> {
                gstInvoiceNo = null
            }

            "gstInvoiceDate" -> {
                gstInvoiceDate = null
            }
        }
    }
}