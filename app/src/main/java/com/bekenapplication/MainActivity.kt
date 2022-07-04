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
            uuid = "bookinghotel-089670156465",
            name = "Jhon Doe",
            email = "jhon-doe@gmail.com",
            phoneNumber = "082130944260",
            publicKey = "w/t3emG0hfX1abxrPAHUHz+U6j28jyn7XlTBblP9dFfi7CxbX6/LYUOGieujdjNN",
            secretKey = "93FxV1E3jlyzy8YZLHguXAcbmHUjcPOfYTGMkyninx/B1yegKCXHGTtPjW8sgsXn"
        )
        val btmOpenProdukBeken = findViewById<Button>(R.id.btmOpenProdukBeken)
        btmOpenProdukBeken.setOnClickListener {
            BekenApp.open(this, mitraPartner)
        }
        BekenApp.observerPaymentOnBackground(BekenApp.FROM_NATIVE) {
            Log.d("MAIN KRMTUNAI", it.toString())
            BekenApp.push(true, "KRMTUNAI", "{\"product\": \"KRMTUNAI\",\"type\": \"transaction\",\"data\": {\"reffid\": \"QWEASD123\"}}")
        }
    }
}
