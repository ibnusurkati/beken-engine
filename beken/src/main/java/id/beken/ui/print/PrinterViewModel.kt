package id.beken.ui.print

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection

class PrinterViewModel: ViewModel() {

    val bluetoothIsEnable = MutableLiveData(true)
    val bluetoothDevices = MutableLiveData<List<BluetoothDevice>>()

    private lateinit var bluetoothAdapter: BluetoothAdapter

    fun checkBluetooth(context: Context) {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        val devices = arrayListOf<BluetoothDevice>()

        bluetoothIsEnable.value = bluetoothAdapter.isEnabled

        if (bluetoothAdapter.isEnabled) {
            val bluetoothDevice: Set<BluetoothDevice> = bluetoothAdapter.bondedDevices
            bluetoothDevice.forEach {
                devices.add(it)
            }
        }

        bluetoothDevices.value = devices
    }

    fun print(macAddress: String, content: String) {
        val device = bluetoothAdapter.bondedDevices.firstOrNull { it.address == macAddress }
        val connection = BluetoothConnection(device)
        if (device != null) {
            val printer = EscPosPrinter(connection, 203, 48f, 32)
            printer.printFormattedText(
                "[C]<u><font size='normal'>TRANSFER TUNAI</font></u>\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                        "[L]  + Size : S\n" +
                        "[L]\n" +
                        "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                        "[L]  + Size : 57/58\n" +
                        "[L]\n" +
                        "[C]--------------------------------\n" +
                        "[R]TOTAL PRICE :[R]34.98e\n" +
                        "[R]TAX :[R]4.23e\n" +
                        "[L]\n" +
                        "[C]================================\n" +
                        "[L]\n" +
                        "[L]<font size='tall'>Customer :</font>\n" +
                        "[L]Raymond DUPONT\n" +
                        "[L]5 rue des girafes\n" +
                        "[L]31547 PERPETES\n" +
                        "[L]Tel : +33801201456\n"
            )
        }
    }

}