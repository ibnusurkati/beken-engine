package id.beken.ui.print

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.beken.R
import id.beken.utils.extensions.onBluetoothNotEnable
import id.beken.utils.extensions.onYes
import id.beken.utils.extensions.setTitleAndDescription

class PrinterThermalActivity : AppCompatActivity() {

    private lateinit var printerAdapter: PrinterAdapter
    private val printerViewModel by viewModels<PrinterViewModel>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer_thermal)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        printerViewModel.checkBluetooth(baseContext)

        val type: String? = intent.getStringExtra("TYPE")
        val content: String? = intent.getStringExtra("CONTENT")
        val sharedPref = getSharedPreferences("${packageName}.beken", Context.MODE_PRIVATE)
        val defaultPrinter = sharedPref.getString("DEFAULT_PRINTER", null)

        val listPrinter = findViewById<RecyclerView>(R.id.list_printer)
        printerAdapter = PrinterAdapter(defaultPrinter) { device ->
            when(type) {
                "SET_DEFAULT" -> {
                    PrinterDialog.build(this).setTitleAndDescription(
                        titleText = "Pilih Sebagai Printer Utama!",
                        descriptionText = "Printer utama akan otomatis di gunakan saat akan melakukan print tanpa harus memilih printer."
                    ).onYes {
                        with(sharedPref.edit()) {
                            putString("DEFAULT_PRINTER", device.address)
                            apply()
                        }
                        printerAdapter.updateDefaultPrinter(device.address)
                        printerAdapter.notifyDataSetChanged()

                        it.dismiss()
                    }
                }
                "SELECT_AND_PRINT" -> {
                    PrinterDialog.build(this).setTitleAndDescription(
                        titleText = "Lanjutkan Print ?",
                        descriptionText = "Aplikasi akan melakukan print data dengan printer yang anda pilih, pastikan printer telah aktif."
                    ).onYes {
                        if (content != null) {
                            printerViewModel.print(device.address, content)
                        } else {
                            Toast.makeText(baseContext, "Printer tidak di temukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        listPrinter.layoutManager = LinearLayoutManager(this)
        listPrinter.adapter = printerAdapter

        printerViewModel.bluetoothIsEnable.observe(this) {
            if (!it) {
                PrinterDialog.build(this).setTitleAndDescription(
                    titleText = "Bluetooth Tidak Aktif!",
                    descriptionText = "Fitur print membutuhkan akses bluetooth, aktifkan bluetooth anda dan ulangi kembali langkah ini."
                ).onBluetoothNotEnable {
                    finish()
                }
            }
        }

        printerViewModel.bluetoothDevices.observe(this) {
            printerAdapter.updateList(it)
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun backToApp(view: View) {
        onBackPressed()
    }
}