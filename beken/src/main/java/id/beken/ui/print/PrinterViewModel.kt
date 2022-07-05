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
            printer.printFormattedText(content)
        }
    }

}