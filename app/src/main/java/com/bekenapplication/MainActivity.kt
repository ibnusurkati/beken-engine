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
            uuid = "ayokenali-0818880882",
            name = "Ade",
            email = "ade@gmail.com",
            phoneNumber = "0818880882",
            publicKey = "3HxBcPfiS0omLy31PpPTfEVU7D1pjvLbY1xf7urdUO+ybVGj4KIpPWjpQWLV7YHH",
            secretKey = "h9Xf1jTH0L9NXvZuKJZ845wl203JUCHqOG2qYA7DFFWovs4EXCNE/nhCaJWOgOMi"
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
