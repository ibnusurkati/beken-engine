package id.beken.utils.extensions

import android.annotation.SuppressLint
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import id.beken.R

fun AlertDialog.setTitleAndDescription(titleText: String, descriptionText: String): AlertDialog {
    val title = this.findViewById<TextView>(R.id.title_printer)!!
    val description = this.findViewById<TextView>(R.id.description_printer)!!

    title.text = titleText
    description.text = descriptionText

    return this
}

@SuppressLint("SetTextI18n")
fun AlertDialog.onBluetoothNotEnable(
    action: (() -> Unit)? = null
) {
    val iconPrinter = this.findViewById<ImageView>(R.id.icon_printer)!!
    val btnYes = this.findViewById<Button>(R.id.btn_yes)!!
    val btnNo = this.findViewById<TextView>(R.id.btn_no)!!

    iconPrinter.setImageResource(R.drawable.ic_bluetooth)
    btnNo.visibility = View.GONE
    btnYes.text = "KEMBALI"

    btnYes.setOnClickListener {
        action?.invoke()
        dismiss()
    }
}

fun AlertDialog.onYes(
    action: ((alert: AlertDialog) -> Unit)? = null
) {
    val btnYes = this.findViewById<Button>(R.id.btn_yes)!!
    val btnNo = this.findViewById<TextView>(R.id.btn_no)!!

    btnYes.setOnClickListener {
        action?.invoke(this)
    }

    btnNo.setOnClickListener {
        dismiss()
    }
}