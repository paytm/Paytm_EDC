package com.paytm.pos.printdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.paytm.printgenerator.BitmapReceiverCallback
import com.paytm.printgenerator.printer.PrintStatusCallBack
import com.paytm.printgenerator.printer.PrintStatusEnum
import com.paytm.printgenerator.printer.Printer
import com.paytm.printgenerator.utils.PrinterUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var bitmap: Bitmap? = null
        val icon: Bitmap = PrinterUtils.bitmapToBlack(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher), 110)

        val callback = object : PrintStatusCallBack {
            override fun onFailure(id: String, status: PrintStatusEnum) {
                addLogText("Printing Failed : $id")
                addLogText( "Message : $status")
            }

            override fun onSuccess(id: String) {
                addLogText("File : $id, Printed Successfully")
            }
        }

        btn_generate.setOnClickListener {
            val page = BitmapGeneratorDemoJava.getDemoPage(this)
            Printer.generateBitmapAsync(page, object : BitmapReceiverCallback {
                override fun onFailure(message: String) {
                    addLogText("Bitmap Generation Failed : $message")
                }

                override fun onSuccess(image: Bitmap) {
                    addLogText("Bitmap Created Successfully")
                    bitmap = image
                    imageView.setImageBitmap(image)
                }
            })
        }

        btn_print.setOnClickListener {
            if (bitmap != null)
                Printer.print(bitmap!!, "temp" + getRandomString(5),this, callback)
            else
                addLogText("Bitmap is null.")
        }

        btn_generate_and_print.setOnClickListener {
            val page = BitmapGeneratorDemo.getDemoPage(icon!!)
            Printer.print(page, "temp" + getRandomString(5),this, callback)
        }

    }

    private fun addLogText(text: String) {
        Log.d("Printing Test", text)
        val temp = log.text.toString() + text + "\n"
        log.text = temp
    }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}