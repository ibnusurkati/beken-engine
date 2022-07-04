package id.beken.ui.webapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.beken.BekenApp
import id.beken.data.repositories.ContactRepository
import id.beken.data.services.TransactionService
import id.beken.models.AuthMitraPartner
import id.beken.models.Contact
import id.beken.models.TransactionData
import id.beken.models.TransactionOutput
import id.beken.ui.camera.IdCardCameraActivity
import id.beken.ui.camera.SelfieCameraActivity
import id.beken.ui.print.PrinterThermalActivity
import id.beken.ui.webapp.transaction.TransactionDialog
import id.beken.ui.webapp.transaction.TransactionPdf
import id.beken.utils.extensions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

@Suppress("BlockingMethodInNonBlockingContext")
class WebAppHandler(
    private val activity: WebAppActivity,
    private val webView: WebView,
    private val authMitraPartner: AuthMitraPartner,
) {

    @JavascriptInterface
    fun authentication(): String {
        val dataAuth = JSONObject()

        dataAuth.put("publicKey", authMitraPartner.publicKey)
        dataAuth.put("secretKey", authMitraPartner.secretKey)
        dataAuth.put("uuid", authMitraPartner.uuid)
        dataAuth.put("name", authMitraPartner.name)
        dataAuth.put("email", authMitraPartner.email)
        dataAuth.put("phoneNumber", authMitraPartner.phoneNumber)

        return dataAuth.toString()
    }

    @JavascriptInterface
    fun closeBeken() {
        activity.finish()
    }

    @JavascriptInterface
    fun contacts(limit: String?, offset: String?): String {
        val contactRepository = ContactRepository(activity.baseContext)
        val listContacts: List<Contact> = if (limit != null && offset != null) {
            contactRepository.findWithLimitOffset(limit, offset)
        } else {
            contactRepository.findAll()
        }
        return Gson().toJson(listContacts)
    }

    @JavascriptInterface
    fun idCardCamera() {
        val intent = Intent(activity, IdCardCameraActivity::class.java)
        activity.requestCamera.launch(intent)
    }

    @JavascriptInterface
    fun selfieCamera() {
        val intent = Intent(activity, SelfieCameraActivity::class.java)
        activity.requestCamera.launch(intent)
    }

    @JavascriptInterface
    fun setDefaultPrinter() {
        val intent = Intent(activity, PrinterThermalActivity::class.java)
        intent.putExtra("TYPE", "SET_DEFAULT")
        activity.startActivity(intent)
    }

    @JavascriptInterface
    fun print(content: String) {
        val sharedPref =
            activity.getSharedPreferences("${activity.packageName}.beken", Context.MODE_PRIVATE)
        val defaultPrinter = sharedPref.getString("DEFAULT_PRINTER", null)
        if (defaultPrinter != null) {
            activity.printerViewModel.print(defaultPrinter, content)
        } else {
            Toast.makeText(activity.baseContext, "Printer tidak di temukan!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @JavascriptInterface
    fun setSelectAndPrint(content: String) {
        val intent = Intent(activity, PrinterThermalActivity::class.java)
        intent.putExtra("TYPE", "SELECT_AND_PRINT")
        intent.putExtra("CONTENT", content)
        activity.startActivity(intent)
    }

    @JavascriptInterface
    fun shareReceipt(data: String) {
        try {
            val context = webView.context
            val transactionOutput = Gson().fromJson(data, TransactionOutput::class.java)
            val content = transactionOutput.content
            val prefixFilename = transactionOutput.prefixFilename

            TransactionPdf.build(context, content).share(context, prefixFilename)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun downloadReceipt(data: String) {
        try {
            val context = webView.context
            val transactionOutput = Gson().fromJson(data, TransactionOutput::class.java)
            val content = transactionOutput.content
            val prefixFilename = transactionOutput.prefixFilename

            TransactionPdf.build(context, content).download(context, prefixFilename)
            Toast.makeText(context, "Berhasil menyimpan file!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JavascriptInterface
    fun paymentCallback(productName: String, data: String) {
        BekenApp.pushFrom(BekenApp.FROM_NATIVE, productName, data)
    }

    @JavascriptInterface
    fun transaction(params: String) {
        val transactionData = Gson().fromJson(params, TransactionData::class.java)
        activity.lifecycleScope.launchWhenResumed {
            TransactionDialog.build(activity)
                .onPositive {
                    it.progress()

                    val publicKey = authMitraPartner.publicKey
                    val secretKey = authMitraPartner.secretKey
                    val signature = (publicKey + secretKey).sha256()

                    val payloadType = object : TypeToken<HashMap<String, String>>() {}.type
                    val payload = Gson().fromJson<HashMap<String, String>>(transactionData.data, payloadType)
                    payload["pin"] = it.getPin()

                    GlobalScope.launch(Dispatchers.Main) {
                        kotlin.runCatching {
                            TransactionService.create(authMitraPartner.debug).trx(
                                transactionData.url,
                                signature,
                                authMitraPartner.publicKey,
                                payload
                            )
                        }.onSuccess { response ->
                            val data = if (response.isSuccessful) {
                                Gson().toJson(response.body())
                            } else {
                                response.errorBody()?.string()
                            }
                            Log.d("DATA TRX", data.toString())

                            webView.post {
                                webView.evaluateJavascript(
                                    "onTransaction(${response.isSuccessful}, '${transactionData.productName}', '${data}')",
                                    null
                                )
                            }
                            if (response.isSuccessful) {
                                it.success("Transaksimu sedang di proses, mohon di tunggu yaa. Terima kasih. :)")
                            } else {

                                val dataError = JSONObject(data ?: "{\"code\":\"E_\"}")
                                if (dataError.getString("code") == "E_PIN_INVALID") {
                                    it.failed("Maaf, pin yang anda masukan tidak valid!")
                                } else {
                                    it.failed("Yaah!. Transaksimu gagal, mungkin sedang gangguan atau terjadi kesalahan, coba ulangi lagi ya. :(")
                                }
                            }
                        }.onFailure { e ->
                            e.printStackTrace()
                            webView.post {
                                webView.evaluateJavascript(
                                    "onTransaction(false, '${transactionData.productName}', null)",
                                    null
                                )
                            }
                            it.error("Yaah!. Transaksimu gagal, mungkin sedang gangguan atau terjadi kesalahan, coba ulangi lagi ya. :(")
                        }
                    }
                }
        }
    }

}