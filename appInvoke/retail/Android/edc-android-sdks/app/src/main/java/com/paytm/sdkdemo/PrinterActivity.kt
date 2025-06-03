package com.paytm.sdkdemo

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.paytm.pos.printdemo.BitmapGeneratorDemo
import com.paytm.printgenerator.BitmapReceiverCallback
import com.paytm.printgenerator.printer.PrintStatusCallBack
import com.paytm.printgenerator.printer.PrintStatusEnum
import com.paytm.printgenerator.printer.Printer
import com.paytm.printgenerator.utils.PrinterUtils

class PrinterActivity : AppCompatActivity() {

    lateinit var log : TextView
    private fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)
        log = findViewById(R.id.log)

        var bitmap: Bitmap? = null

        val ic = PrinterUtils.bitmapToBlack(
            getBitmapFromVectorDrawable(R.drawable.ic_all_in_one_pos),
            110
        )

        val callback = object : PrintStatusCallBack {
            override fun onFailure(id: String, status: PrintStatusEnum) {
                addLogText("Printing Failed : $id")
                addLogText("Message : $status")
            }

            override fun onPrintStarted(id: String) {
                addLogText("File : $id, Printing Started")
            }

            override fun onSuccess(id: String) {
                addLogText("File : $id, Printed Successfully")
            }
        }

        findViewById<Button>(R.id.btn_generate).setOnClickListener {
            val page = BitmapGeneratorDemoJava.getDemoPage(this)
            Printer.generateBitmapAsync(page, object : BitmapReceiverCallback {
                override fun onFailure(message: String) {
                    addLogText("Bitmap Generation Failed : $message")
                }

                override fun onSuccess(image: Bitmap) {
                    addLogText("Bitmap Created Successfully")
                    bitmap = image
                    findViewById<ImageView>(R.id.imageView).setImageBitmap(image)
                }
            })
        }

        findViewById<Button>(R.id.btn_print).setOnClickListener {
            if (bitmap != null) {
                addLogText("Thread Name: ${Thread.currentThread().name} , ${Thread.currentThread().id}")
                Printer.print(bitmap!!, "temp" + getRandomString(5), this, callback)
            } else
                addLogText("Bitmap is null.")
        }

        findViewById<Button>(R.id.btn_generate_and_print).setOnClickListener {
            addLogText("Thread Name: ${Thread.currentThread().name} , ${Thread.currentThread().id}")
            val page = BitmapGeneratorDemo.getDemoPage(ic)
            Printer.print(page, "temp" + getRandomString(5), this, callback)
        }

        findViewById<Button>(R.id.btn_generate_and_print_on_background).setOnClickListener {
            Thread {
                addLogText("Thread Name: ${Thread.currentThread().name} , ${Thread.currentThread().id}")
                val page = BitmapGeneratorDemo.getDemoPage(ic)
                Printer.printInBackground(page, "temp" + getRandomString(5), this, callback)
            }.start()
        }

        findViewById<Button>(R.id.btn_print_on_background).setOnClickListener {
            Thread {
                if (bitmap != null) {
                    addLogText("Thread Name: ${Thread.currentThread().name} , ${Thread.currentThread().id}")
                    Printer.printInBackground(bitmap!!, "temp" + getRandomString(5), this, callback)
                } else
                    addLogText("Bitmap is null.")
            }.start()
        }

    }

    private fun addLogText(text: String) {
        Log.d("Printing Test", text)
        runOnUiThread {
            val temp = log.text.toString() + text + "\n"
            log.text = temp
        }
    }

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}