package paytm.edc.ecrclient

import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import kotlinx.android.synthetic.main.activity_main.connect
import kotlinx.android.synthetic.main.activity_main.disconnect
import kotlinx.android.synthetic.main.activity_main.displayHome
import kotlinx.android.synthetic.main.activity_main.displayQr
import kotlinx.android.synthetic.main.activity_main.displaySuccess
import kotlinx.android.synthetic.main.activity_main.qrAmount
import kotlinx.android.synthetic.main.activity_main.qrData
import paytm.edc.dqr.DqrCommunicator
import paytm.edc.dqr.DeviceStateCode
import paytm.edc.dqr.DeviceStateListener

class MainActivity : AppCompatActivity(), DeviceStateListener {

    private lateinit var dqrCommunicator: DqrCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dqrCommunicator = DqrCommunicator.getInstance(this)
        dqrCommunicator.setDeviceStateListener(this)
        setListeners()
    }

    private fun setListeners() {

        connect.setOnClickListener {
            dqrCommunicator.connect()
        }
        displayHome.setOnClickListener {
            if (dqrCommunicator.isConnected()) {
                Toast.makeText(
                    this,
                    "Displayed Home : " + dqrCommunicator.showHomeScreen(),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Not Connected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        displayQr.setOnClickListener {
            val qrString = qrData.text.toString().trim()
            if (qrString.isEmpty()) {
                Toast.makeText(this, "Please enter a valid QR string", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val qrAmount = qrAmount.text.toString().trim()

            if (qrAmount.isEmpty()) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dqrCommunicator.isConnected()) {
                Toast.makeText(
                    this,
                    "Displayed QR : " + dqrCommunicator.displayTxnQr(qrString, qrAmount),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Not Connected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        displaySuccess.setOnClickListener {
            val qrAmount = qrAmount.text.toString().trim()

            if (qrAmount.isEmpty()) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (dqrCommunicator.isConnected()) {
                Toast.makeText(
                    this,
                    "Displayed Success : " + dqrCommunicator.displaySuccessScreen(qrAmount),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Not Connected",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        disconnect.setOnClickListener {
            dqrCommunicator.disconnect()
        }
    }

    override fun onStatusChange(deviceStateCode: DeviceStateCode) {
        runOnUiThread {
            when (deviceStateCode) {

                DeviceStateCode.DEVICE_FOUND -> Toast.makeText(
                    this,
                    "Device Found",
                    Toast.LENGTH_SHORT
                )
                    .show()

                DeviceStateCode.DEVICE_NOT_FOUND -> Toast.makeText(
                    this,
                    "Device Not Found",
                    Toast.LENGTH_SHORT
                )
                    .show()

                DeviceStateCode.PERMISSION_DENIED -> Toast.makeText(
                    this,
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()

                DeviceStateCode.CONNECTED -> Toast.makeText(
                    this,
                    "Connected",
                    Toast.LENGTH_SHORT
                ).show()

                DeviceStateCode.DEVICE_ATTACHED -> Toast.makeText(
                    this,
                    "Device Attached",
                    Toast.LENGTH_SHORT
                ).show()

                DeviceStateCode.DEVICE_DETACHED -> Toast.makeText(
                    this,
                    "Device Detached",
                    Toast.LENGTH_SHORT
                ).show()

                DeviceStateCode.CONNECT_FAILED -> Toast.makeText(
                    this,
                    "Connect Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}