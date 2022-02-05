package id.beken.ui.webapp.transaction

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import id.beken.R

class TransactionDialog {

    companion object {
        private lateinit var layoutInflater: LayoutInflater

        fun build(context: Context): AlertDialog {
            layoutInflater = LayoutInflater.from(context)
            val alertDialog =
                AlertDialog.Builder(
                    context, R.style.Theme_Beken_DialogTransaction
                )
                    .setView(R.layout.transaction_dilaog)
            val alert: AlertDialog = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
            return alert
        }
    }

}