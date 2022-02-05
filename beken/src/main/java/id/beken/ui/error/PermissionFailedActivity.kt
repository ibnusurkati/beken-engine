package id.beken.ui.error

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import id.beken.R
import id.beken.ui.webapp.WebAppActivity

class PermissionFailedActivity : AppCompatActivity() {

    @Suppress("PrivatePropertyName")
    private var PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_failed)
    }

    fun backToFirst(view: View) {
        super.onBackPressed()
    }

    fun giveAccess(view: View) {
        requestPermissionLauncher.launch(PERMISSIONS)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permission ->
            val check: Boolean? = permission.values.firstOrNull{ it == false }
            if (check != null && !check) {
                findViewById<TextView>(R.id.permission_failed_msg).visibility = View.VISIBLE
                Log.d("ERROR", "Gagal mendapatkan Access")
            } else {
                val intent = Intent(this, WebAppActivity::class.java)
                startActivity(intent)
            }
        }
}