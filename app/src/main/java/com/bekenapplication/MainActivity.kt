package com.bekenapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import id.beken.BekenApp
import id.beken.models.AuthMitraPartner

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mitraPartner = AuthMitraPartner(
            uuid = "TDYTwBHwMQMjVFEnNXHRPHnPf402",
            name = "Saipul Hidayat",
            email = "im.saipulhidayat@gmail.com",
            phoneNumber = "085382524285",
            publicKey = "XSaLrfFGrGp02NEEaCstv2oMDkhvA8Iz12odB1i6eIdaz0DwglpXjVuO4No/r8Iq",
            secretKey = "/wqlzn6CdYXaSLTXI58FHELl5p8xYMZqhmEhdg/SAWRtPM5ubJiow2lO5gpdgQOA"
        )
        val btmOpenProdukBeken = findViewById<Button>(R.id.btmOpenProdukBeken)
        btmOpenProdukBeken.setOnClickListener {
            BekenApp.open(this, mitraPartner)
        }
        BekenApp.observerPaymentOnBackground(BekenApp.FROM_NATIVE) {
            Log.d("MAIN KRMTUNAI", it.toString())
            BekenApp.push(true, "KRMTUNAI", "{\"message\":\"Success fully\"}")
        }
    }
}
