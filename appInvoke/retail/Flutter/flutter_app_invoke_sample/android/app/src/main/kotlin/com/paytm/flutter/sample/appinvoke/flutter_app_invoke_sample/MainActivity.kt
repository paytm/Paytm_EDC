package com.paytm.flutter.sample.appinvoke.flutter_app_invoke_sample

import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.paytm.pos.app_invoke_sdk.PaytmPayments
import com.paytm.printgenerator.Alignment
import com.paytm.printgenerator.FontSize
import com.paytm.printgenerator.page.Page
import com.paytm.printgenerator.page.TextElement
import com.paytm.printgenerator.printer.PrintStatusCallBack
import com.paytm.printgenerator.printer.PrintStatusEnum
import com.paytm.printgenerator.printer.Printer
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.Result

class MainActivity : FlutterActivity(), PaytmPayments.IPaytmResult {


    private val TAG: String =
        MainActivity::class.java.simpleName

    private val channelAppInvokeName = "appInvoke"
    private val channelPrinterName = "printer"

    private var resultAppInvoke: Result? = null
    private var resultPrint: Result? = null
    private var saleRequestId = System.currentTimeMillis().toString()
    private var statusCheckRequestId = System.currentTimeMillis().toString()
    private var voidRequestId = System.currentTimeMillis().toString()
    private var printRequestId = System.currentTimeMillis().toString()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val channelPayments =
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelAppInvokeName)

        val channelPrinter =
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelPrinterName)


        channelPrinter.setMethodCallHandler { call, result ->
            this.resultPrint = result
            when (call.method) {
                "printData" -> {
                    val data = call.arguments<Map<String, Any>>() ?: return@setMethodCallHandler
                    val page = Page()

                    for (text in data) {
                        if (text.value !is String) continue
                        page.addLine().addElement(
                            TextElement(
                                false,
                                false,
                                FontSize.FONT_NORMAL,
                                text.key,
                                Typeface.SANS_SERIF,
                                Alignment.CENTER,
                                0,
                                1
                            )
                        ).addElement(
                            TextElement(
                                false,
                                false,
                                FontSize.FONT_NORMAL,
                                text.value as String,
                                Typeface.SANS_SERIF,
                                Alignment.CENTER,
                                0,
                                1
                            )
                        )
                    }

                    page.addLine()
                        .addElement(
                            TextElement(
                                false,
                                false,
                                FontSize.FONT_NORMAL,
                                "SANS_SERIF",
                                Typeface.SANS_SERIF,
                                Alignment.CENTER,
                                0,
                                1
                            )
                        )
                    page.addLine().addElement(
                        TextElement(
                            false,
                            false,
                            FontSize.FONT_NORMAL,
                            "SERIF",
                            Typeface.SERIF,
                            Alignment.CENTER,
                            0,
                            1
                        )
                    )
                    page.addLine().addElement(
                        TextElement(
                            false,
                            false,
                            FontSize.FONT_NORMAL,
                            "NULL",
                            null,
                            Alignment.CENTER,
                            0,
                            1
                        )
                    )
                    page.addLine().addElement(
                        TextElement(
                            false,
                            false,
                            FontSize.FONT_NORMAL,
                            "DEFAULT",
                            Typeface.DEFAULT,
                            Alignment.CENTER,
                            0,
                            1
                        )
                    )
                    page.addLine().addElement(
                        TextElement(
                            false,
                            false,
                            FontSize.FONT_NORMAL,
                            "DEFAULT_BOLD",
                            Typeface.DEFAULT_BOLD,
                            Alignment.CENTER,
                            0,
                            1
                        )
                    )
                    printRequestId = System.currentTimeMillis().toString()
                    with(Printer.Companion) {
                        print(page, printRequestId, this@MainActivity, object :
                            PrintStatusCallBack {
                            override fun onSuccess(id: String) {
                                resultPrint?.success(
                                    mapOf(
                                        "requestId" to id,
                                        "message" to "print success"
                                    )
                                )
                            }

                            override fun onPrintStarted(id: String) {
                                Log.d(TAG, "Print started with id : $id")
                            }

                            override fun onFailure(id: String, status: PrintStatusEnum) {
                                resultPrint?.error(
                                    status.code.toString(),
                                    status.getMessage(),
                                    status.getMessage()
                                )
                            }
                        })
                    }

                }

                "printBitmap" -> {
                    val imageData = call.arguments<ByteArray>()
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData!!.size)
                    printRequestId = System.currentTimeMillis().toString()
                    with(Printer.Companion) {
                        print(bitmap, printRequestId, this@MainActivity, object :
                            PrintStatusCallBack {
                            override fun onSuccess(id: String) {
                                resultPrint?.success(
                                    mapOf(
                                        "requestId" to id,
                                        "message" to "print success"
                                    )
                                )
                            }

                            override fun onPrintStarted(id: String) {
                                Log.d(TAG, "Print started with id : $id")
                            }

                            override fun onFailure(id: String, status: PrintStatusEnum) {
                                resultPrint?.error(
                                    status.code.toString(),
                                    status.getMessage(),
                                    status.getMessage()
                                )
                            }
                        })

                    }
                }
            }
        }

        channelPayments.setMethodCallHandler { call, result ->
            when (call.method) {

                "sale" -> {
                    this.resultAppInvoke = result
                    val arguments = call.arguments as Map<*, *>
                    val amount = arguments["amount"] as String
                    val orderId = arguments["orderId"] as String
                    val paymentMode = arguments["paymentMode"] as String

                    if (validateInput(amount, orderId)) {
                        saleRequestId = PaytmPayments.doPayment(
                            activity = this,
                            paytmResult = this,
                            amountInPaise = amount.toInt(),
                            orderId = orderId,
                            payMode = paymentMode, //Optional
                        ).toString()
                    } else {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    }
                }

                "void" -> {
                    this.resultAppInvoke = result
                    val arguments = call.arguments as Map<*, *>
                    val orderId = arguments["orderId"] as String

                    if (validateInput(null, orderId)) {
                        voidRequestId = PaytmPayments.doVoid(
                            activity = this,
                            paytmResult = this,
                            orderId = orderId
                        ).toString()
                    } else {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    }
                }

                "status" -> {
                    this.resultAppInvoke = result
                    val arguments = call.arguments as Map<*, *>
                    val orderId = arguments["orderId"] as String

                    if (validateInput(null, orderId)) {
                        statusCheckRequestId = PaytmPayments.checkStatus(
                            activity = this,
                            paytmResult = this,
                            orderId = orderId,
                        ).toString()
                    } else {
                        Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInput(
        amountInPaise: String?,
        orderId: String?
    ): Boolean {
        var isValid = true
        if (amountInPaise != null && (TextUtils.isEmpty(amountInPaise) || amountInPaise.length > 11)) {
            isValid = false
        }
        if (orderId != null && (TextUtils.isEmpty(orderId) || !orderId.matches("[a-zA-Z0-9]*".toRegex()))) {
            isValid = false
        }
        return isValid
    }

    override fun onError(requestId: Int, errorMsg: String) {
        resultAppInvoke?.error(requestId.toString(), errorMsg, errorMsg)
    }

    override fun onResult(requestId: Int, responseData: Map<String, String>) {
        resultAppInvoke?.success(
            mapOf(
                "requestId" to requestId.toString(),
                "responseData" to responseData
            )
        )
    }
}

