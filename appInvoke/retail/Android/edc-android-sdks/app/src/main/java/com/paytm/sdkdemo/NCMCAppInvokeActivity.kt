package com.paytm.sdkdemo

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paytm.pos.app_invoke_sdk.NcmcPayments

/**
 * Demo screen for the NCMC (National Common Mobility Card) flows exposed by
 * [NcmcPayments]: read card (transit), add money, update balance, create service
 * and check balance. Kept separate from [AppInvokeActivity] so the generic
 * payment / closed-loop card demo stays focused on [com.paytm.pos.app_invoke_sdk.PaytmPayments].
 */
class NCMCAppInvokeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etAmount: EditText
    private lateinit var etOrderId: EditText
    private lateinit var etCardSHA: EditText
    private lateinit var etServiceType: EditText
    private lateinit var etKey: EditText
    private lateinit var etValue: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnRemove: Button
    private lateinit var rdFlowType: RadioGroup
    private lateinit var isOSA: RadioButton
    private lateinit var btnAddMoney: Button
    private lateinit var btnBalanceUpdate: Button
    private lateinit var btnCheckBalance: Button
    private lateinit var btnCreateService: Button
    private lateinit var btnReadCardTransit: Button
    private lateinit var tvRequest: TextView
    private lateinit var tvResponse: TextView
    private lateinit var btnReset: Button

    private val addMoney = R.id.btn_add_money
    private val updateBalance = R.id.btn_balance_update
    private val checkBalance = R.id.btn_check_balance
    private val createService = R.id.btn_create_service
    private val readCardTransit = R.id.btn_read_card_transit
    private val add = R.id.add
    private val remove = R.id.remove
    private val reset = R.id.reset

    private var flowType = "closed"
    private var startTime: Long = 0

    private val extendInfoMap = HashMap<String, String>()

    /** Renders every NCMC SDK callback (result or error) into the response TextView with elapsed time. */
    private val ncmcPaytmResponseListener = object : NcmcPayments.IPaytmResult {
        override fun onResult(requestId: Int, responseData: Map<String, String>) {
            val time = "${System.currentTimeMillis() - startTime}ms"
            startTime = System.currentTimeMillis()
            val message = "RESPONSE\n" +
                    "Time : $time \n" +
                    "onResult -> \n" +
                    "Code : $requestId \n" +
                    "Data : $responseData"
            tvResponse.text = message
        }

        override fun onError(requestId: Int, errorMsg: String) {
            val time = "${System.currentTimeMillis() - startTime}ms"
            startTime = System.currentTimeMillis()
            val message = "RESPONSE\n" +
                    "Time : $time \n" +
                    "onError -> \n$requestId : $errorMsg"
            tvResponse.text = message
        }
    }

    /** Inflates the NCMC layout, binds views and wires click listeners. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ncmc_app_invoke)
        initViewProperties()
        setClickListeners()
        refreshRequestTv(0, "")
    }

    /** Looks up every view referenced by this screen from the inflated layout. */
    private fun initViewProperties() {
        etAmount = findViewById(R.id.amount)
        etOrderId = findViewById(R.id.orderId)
        etCardSHA = findViewById(R.id.cardSHA)
        etServiceType = findViewById(R.id.serviceType)
        etKey = findViewById(R.id.key)
        etValue = findViewById(R.id.value)
        btnAdd = findViewById(add)
        btnRemove = findViewById(remove)
        rdFlowType = findViewById(R.id.rdFlowType)
        isOSA = findViewById(R.id.isOSA)
        btnAddMoney = findViewById(addMoney)
        btnBalanceUpdate = findViewById(updateBalance)
        btnCheckBalance = findViewById(checkBalance)
        btnCreateService = findViewById(createService)
        btnReadCardTransit = findViewById(readCardTransit)
        tvRequest = findViewById(R.id.tv_request)
        tvResponse = findViewById(R.id.tv_response)
        btnReset = findViewById(reset)
    }

    /** Reads an NCMC card; flowType must be "transit" for this call to be meaningful. */
    private fun initiateReadCardTransit(orderId: String, flowType: String, isOSA: Boolean) {
        NcmcPayments.doReadCard(
            activity = this,
            paytmResult = ncmcPaytmResponseListener,
            orderId = orderId,
            flowType = flowType,      // must be "transit"
            isOsa = isOSA,
            readMethod = "tap",
            requestTags = listOf("5A", "DF33")
        )
    }

    /** Tops up the NCMC card identified by cardNoSHA with amountInPaise for the given serviceType. */
    private fun initiateAddMoney(
        amountInPaise: Int,
        orderId: String,
        cardNoSHA: String,
        serviceType: String,
        flowType: String
    ) {
        NcmcPayments.doMoneyAdd(
            activity = this,
            paytmResult = ncmcPaytmResponseListener,
            amountInPaise = amountInPaise,
            orderId = orderId,
            extendInfo = extendInfoMap, //Optional
            cardNoSHA = cardNoSHA,
            serviceType = serviceType,
            flowType = flowType
        )
    }

    /** Writes the pending balance update onto the NCMC card identified by cardNoSHA. */
    private fun initiateUpdateBalance(orderId: String, cardNoSHA: String, flowType: String) {
        NcmcPayments.doUpdateBalance(
            activity = this,
            paytmResult = ncmcPaytmResponseListener,
            orderId = orderId,
            extendInfo = extendInfoMap, //Optional
            cardNoSHA = cardNoSHA,
            flowType = flowType
        )
    }

    /** Creates a service area on the NCMC card for the selected flowType. */
    private fun initiateCreateService(orderId: String, flowType: String, isOSA: Boolean) {
        NcmcPayments.doCreateService(
            activity = this,
            paytmResult = ncmcPaytmResponseListener,
            orderId = orderId,
            extendInfo = extendInfoMap, //Optional
            flowType = flowType,
            isOsa = isOSA
        )
    }

    /** Reads the current balance of the tapped NCMC card for the selected flowType. */
    private fun initiateCheckBalance(orderId: String, flowType: String) {
        NcmcPayments.doCheckBalance(
            activity = this,
            paytmResult = ncmcPaytmResponseListener,
            orderId = orderId,
            flowType = flowType,
            requestTags = listOf("5A", "DF33")
        )
    }

    /** Registers this activity as the click handler for every action button and radio group. */
    private fun setClickListeners() {
        btnAdd.setOnClickListener(this)
        btnRemove.setOnClickListener(this)
        btnReset.setOnClickListener(this)
        btnAddMoney.setOnClickListener(this)
        btnBalanceUpdate.setOnClickListener(this)
        btnCreateService.setOnClickListener(this)
        btnCheckBalance.setOnClickListener(this)
        btnReadCardTransit.setOnClickListener(this)

        rdFlowType.setOnCheckedChangeListener { _, checkedId ->
            flowType = findViewById<RadioButton>(checkedId).text.toString()
            val transitSelected = checkedId == R.id.transit
            isOSA.isEnabled = transitSelected
            if (!transitSelected) {
                isOSA.isChecked = false
            }
        }
    }

    /** Dispatches a button click to the matching NCMC SDK call using the current form values. */
    override fun onClick(v: View?) {
        val amountString = etAmount.text.toString()
        val amount = if (amountString.isNotEmpty()) Integer.valueOf(amountString) else 0
        val orderId = etOrderId.text.toString()
        val cardNoSHA = etCardSHA.text.toString()
        val serviceType = etServiceType.text.toString()
        val key = etKey.text.toString()
        val value = etValue.text.toString()
        startTime = System.currentTimeMillis()
        when (v?.id) {
            add -> {
                addParamToExtendInfo(key, value)
            }

            remove -> {
                removeParamFromExtendInfo(key)
            }

            reset -> {
                clearForm()
            }

            readCardTransit -> {
                initiateReadCardTransit(orderId, flowType, isOSA.isChecked)
            }

            addMoney -> {
                initiateAddMoney(amount, orderId, cardNoSHA, serviceType, flowType)
            }

            updateBalance -> {
                initiateUpdateBalance(orderId, cardNoSHA, flowType)
            }

            createService -> {
                initiateCreateService(orderId, flowType, isOSA.isChecked)
            }

            checkBalance -> {
                initiateCheckBalance(orderId, flowType)
            }
        }
        refreshRequestTv(amount, orderId)
    }

    /** Clears all inputs and the extendInfo map, then resets the request preview. */
    private fun clearForm() {
        etAmount.text = null
        etOrderId.text = null
        etCardSHA.text = null
        etServiceType.text = null
        etKey.text = null
        etValue.text = null
        extendInfoMap.clear()
        refreshRequestTv(0, "")
    }

    /** Re-renders the request preview TextView from the current form state. */
    private fun refreshRequestTv(amountInPaise: Int, orderId: String) {
        val request = "REQUEST\n" +
                "Amount : $amountInPaise \n" +
                "Order ID : $orderId \n" +
                "FlowType : $flowType \n" +
                "isOSA : ${isOSA.isChecked} \n" +
                "ExtendInfo : $extendInfoMap \n" +
                "CardSHA ${etCardSHA.text}\n" +
                "ServiceType ${etServiceType.text}\n"

        tvRequest.text = request
    }

    /** Adds a key/value pair to the extendInfo map that is passed to the NCMC SDK calls. */
    private fun addParamToExtendInfo(key: String, value: String) {
        if (key.isEmpty() || value.isEmpty()) return
        extendInfoMap[key] = value
        Toast.makeText(this, "$key added in Extend Map", Toast.LENGTH_SHORT).show()
    }

    /** Removes a key from the extendInfo map. */
    private fun removeParamFromExtendInfo(key: String) {
        if (key.isEmpty()) return
        extendInfoMap.remove(key)
        Toast.makeText(this, "$key removed from Extend Map", Toast.LENGTH_SHORT).show()
    }
}
