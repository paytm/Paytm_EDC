package com.paytm.sdkdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.app_invoke).setOnClickListener {
            startActivity(Intent(this, AppInvokeActivity::class.java))
        }
        findViewById<Button>(R.id.printer).setOnClickListener {
            startActivity(Intent(this, PrinterActivity::class.java))
        }
    }
}