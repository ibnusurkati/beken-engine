package id.beken.ui.print

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import id.beken.R

class PrinterAdapter(
    private var defaultPrinter: String?,
    private val action: (device: BluetoothDevice) -> Unit
) : RecyclerView.Adapter<PrinterAdapter.ViewHolder>() {

    private var printers = listOf<BluetoothDevice>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemPrinter: ConstraintLayout = view.findViewById(R.id.item_printer)
        val printerName: TextView = view.findViewById(R.id.printer_name)
        val printerMacAddress: TextView = view.findViewById(R.id.printer_mac_address)
        val printerStatus: ImageView = view.findViewById(R.id.printer_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_printer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val printer = printers[position]

        holder.printerName.text = printer.name
        holder.printerMacAddress.text = printer.address
        holder.printerStatus.visibility =
            if (defaultPrinter != null && defaultPrinter == printer.address) {
                View.VISIBLE
            } else {
                View.GONE
            }
        holder.itemPrinter.setOnClickListener {
            action.invoke(printer)
        }
    }

    override fun getItemCount(): Int = printers.count()

    fun updateList(data: List<BluetoothDevice>) {
        printers = data
    }

    fun updateDefaultPrinter(data: String) {
        defaultPrinter = data
    }
}