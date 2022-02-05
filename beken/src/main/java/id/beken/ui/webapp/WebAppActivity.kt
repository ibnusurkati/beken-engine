package id.beken.ui.webapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.view.KeyEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import id.beken.BekenApp
import id.beken.BuildConfig
import id.beken.R
import id.beken.models.AuthMitraPartner
import id.beken.ui.error.PermissionFailedActivity
import id.beken.ui.print.PrinterViewModel
import id.beken.ui.webapp.state.MitraPartnerState
import id.beken.ui.webapp.transaction.TransactionDialog
import id.beken.utils.extensions.failedRegisterMitraPartner
import java.io.File

@Suppress("PrivatePropertyName")
class WebAppActivity : AppCompatActivity() {

    private lateinit var webApp: WebView
    private lateinit var authMitraPartner: AuthMitraPartner
    private val webAppViewModel by viewModels<WebAppViewModel>()

    val printerViewModel by viewModels<PrinterViewModel>()

    private var PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_app)

        webApp = findViewById(R.id.web_app)

        authMitraPartner = intent.getSerializableExtra("MITRA") as AuthMitraPartner

        webAppViewModel.mitraPartnerState.observe(this, registerMitraPartnerObserver)
        webAppViewModel.isLoading.observe(this, loadindObserver)

        webAppViewModel.registerMitraPartner(authMitraPartner)
        if (!hasPermissions(this, *PERMISSIONS)) {
            requestPermissionLauncher.launch(PERMISSIONS)
        }

        BekenApp.observerPaymentOnBackground(BekenApp.FROM_PARTNER) {
            webApp.post {
                webApp.evaluateJavascript(
                    "onTransaction(${it.status}, '${it.productName}', '${it.data}')",
                    null
                )
            }
        }
    }

    private val loadindObserver = Observer<Boolean> {
        val loaderLogoBeken = findViewById<ProgressBar>(R.id.loader_logo_beken)
        if (it) {
            loaderLogoBeken.visibility = View.VISIBLE
            webApp.visibility = View.GONE
        } else {
            loaderLogoBeken.visibility = View.GONE
            webApp.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private val registerMitraPartnerObserver = Observer<MitraPartnerState> {
        when (it) {
            is MitraPartnerState.Success -> {
                webApp.settings.domStorageEnabled = true
                webApp.settings.databaseEnabled = true
                webApp.settings.cacheMode = WebSettings.LOAD_NO_CACHE
                webApp.settings.javaScriptEnabled = true
                webApp.webViewClient = WebAppClient { status -> webAppViewModel.setLoading(status) }
                webApp.addJavascriptInterface(
                    WebAppHandler(this, webApp, authMitraPartner),
                    "BekenNative"
                )
                webApp.loadUrl(BuildConfig.HOME_URL)
            }
            is MitraPartnerState.Error -> {
                TransactionDialog.build(this).failedRegisterMitraPartner(
                    this,
                    it.message ?: "E_UNKNOWN_ERROR"
                )
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            val check: Boolean? = permission.values.firstOrNull { !it }
            if (check != null && !check) {
                val intent = Intent(this, PermissionFailedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    val requestCamera =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                val intent: Intent = it.data!!
                val file = File(intent.data.toString())
                val fileBase64 = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)

                when(intent.getStringExtra("TYPE")) {
                    "IDCARD" -> {
                        webApp.evaluateJavascript("onIdCardCamera('$fileBase64')", null)
                    }
                    "SELFIE" -> {
                        webApp.evaluateJavascript("onSelfieCamera('$fileBase64')", null)
                    }
                }

                file.delete()
            }
        }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webApp.canGoBack()) {
            webApp.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}