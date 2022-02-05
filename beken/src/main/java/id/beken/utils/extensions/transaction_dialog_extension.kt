package id.beken.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import id.beken.R
import id.beken.ui.webapp.transaction.TransactionPin

fun AlertDialog.onPositive(
    action: ((alert: AlertDialog) -> Unit)? = null
) {
    val transactionPin = this.findViewById<TransactionPin>(R.id.transaction_pin)!!
    val btnConfirmation = this.findViewById<Button>(R.id.btn_confirmation)!!
    val processTransaction = this.findViewById<ProgressBar>(R.id.process_transaction)!!
    val failedMessage = this.findViewById<TextView>(R.id.failed_message)!!
    val btnCancel = this.findViewById<TextView>(R.id.btn_cancel)!!

    transactionPin.visibility = View.VISIBLE
    transactionPin.doOnTextChanged { text, _, _, _ ->
        failedMessage.visibility = View.GONE
        val textPin = text.toString()
        val imm: InputMethodManager = transactionPin.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        btnConfirmation.isEnabled = textPin.length == 6
        if (textPin.length == 6) {
            imm.hideSoftInputFromWindow(transactionPin.windowToken, 0)
        }
    }
    btnConfirmation.visibility = View.VISIBLE
    processTransaction.visibility = View.GONE

    btnConfirmation.setOnClickListener {
        action?.invoke(this)
    }

    btnCancel.setOnClickListener {
        dismiss()
    }
}

fun AlertDialog.getPin(): String {
    return this.findViewById<TransactionPin>(R.id.transaction_pin)!!.text.toString()
}

fun AlertDialog.progress() {
    this.findViewById<TransactionPin>(R.id.transaction_pin)!!.visibility = View.GONE
    this.findViewById<Button>(R.id.btn_confirmation)!!.visibility = View.GONE
    this.findViewById<TextView>(R.id.btn_cancel)!!.visibility = View.GONE
    this.findViewById<ProgressBar>(R.id.process_transaction)!!.visibility = View.VISIBLE
}

fun AlertDialog.failed(message: String) {
    val transactionPin = this.findViewById<TransactionPin>(R.id.transaction_pin)!!
    val failedMessage = this.findViewById<TextView>(R.id.failed_message)!!

    transactionPin.visibility = View.VISIBLE
    transactionPin.text?.clear()

    failedMessage.visibility = View.VISIBLE
    failedMessage.text = message.trim()

    this.findViewById<Button>(R.id.btn_confirmation)!!.visibility = View.VISIBLE
    this.findViewById<TextView>(R.id.btn_cancel)!!.visibility = View.VISIBLE
    this.findViewById<ProgressBar>(R.id.process_transaction)!!.visibility = View.GONE
}

@SuppressLint("SetTextI18n")
fun AlertDialog.success(message: String) {
    this.run {  }
    val title = this.findViewById<TextView>(R.id.transaction_title)!!
    val description = this.findViewById<TextView>(R.id.transaction_description)!!
    val iconTransaction = this.findViewById<ImageView>(R.id.icon_transaction)!!
    val btnFinish = this.findViewById<Button>(R.id.btn_finish)!!

    this.findViewById<ProgressBar>(R.id.process_transaction)!!.visibility = View.GONE

    iconTransaction.setImageResource(R.drawable.ic_success)

    title.text = "Transaksi Berhasil"
    description.text = message.trim()

    btnFinish.visibility = View.VISIBLE
    btnFinish.setOnClickListener {
        dismiss()
    }
}

@SuppressLint("SetTextI18n")
fun AlertDialog.error(message: String) {
    val title = this.findViewById<TextView>(R.id.transaction_title)!!
    val description = this.findViewById<TextView>(R.id.transaction_description)!!
    val iconTransaction = this.findViewById<ImageView>(R.id.icon_transaction)!!
    val btnFinish = this.findViewById<Button>(R.id.btn_finish)!!

    this.findViewById<ProgressBar>(R.id.process_transaction)!!.visibility = View.GONE

    iconTransaction.setImageResource(R.drawable.ic_error)
    title.text = "Transaksi Gagal!"
    description.text = message.trim()

    btnFinish.visibility = View.VISIBLE
    btnFinish.setOnClickListener {
        dismiss()
    }
}

@SuppressLint("SetTextI18n")
fun AlertDialog.failedRegisterMitraPartner(activity: Activity, code: String) {
    val title = this.findViewById<TextView>(R.id.transaction_title)!!
    val description = this.findViewById<TextView>(R.id.transaction_description)!!
    val iconTransaction = this.findViewById<ImageView>(R.id.icon_transaction)!!
    val failedMessage = this.findViewById<TextView>(R.id.failed_message)!!
    val btnBackToFirst = this.findViewById<Button>(R.id.btn_back_to_first)!!

    this.findViewById<ProgressBar>(R.id.process_transaction)!!.visibility = View.GONE
    this.findViewById<TransactionPin>(R.id.transaction_pin)!!.visibility = View.GONE
    this.findViewById<Button>(R.id.btn_confirmation)!!.visibility = View.GONE
    this.findViewById<TextView>(R.id.btn_cancel)!!.visibility = View.GONE

    iconTransaction.setImageResource(R.drawable.ic_error)

    btnBackToFirst.visibility = View.VISIBLE
    failedMessage.visibility = View.VISIBLE
    failedMessage.text = code.trim()
    title.text = "Maaf Terjadi Kesalahan"
    description.text = "Maaf telah terjadi kesalahan sistem, silahkan coba kembali beberapa saat lagi"

    btnBackToFirst.setOnClickListener {
        activity.finish()
    }
}