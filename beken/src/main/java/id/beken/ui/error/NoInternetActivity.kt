package id.beken.ui.error

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import id.beken.R

class NoInternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
    }

    fun backToFirst(view: View) {
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}