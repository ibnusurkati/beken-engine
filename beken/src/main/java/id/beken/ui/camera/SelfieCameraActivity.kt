package id.beken.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Size
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import id.beken.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SelfieCameraActivity : AppCompatActivity() {

    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture = ImageCapture.Builder()
        .setTargetResolution(Size(640, 880))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selfie_camera)

        startCamera()
        outputDirectory = getOutputDirectory()
        val cameraBack = findViewById<ImageView>(R.id.btn_take_selfie)
        cameraBack.setOnClickListener {
            takePhoto()
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val viewFinder = findViewById<PreviewView>(R.id.selfie_camera)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(viewFinder.surfaceProvider)
                    }

                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageCapture)
                    .build()

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
                } catch (exc: Exception) {
                    exc.printStackTrace()
                    Toast.makeText(baseContext, exc.message, Toast.LENGTH_SHORT)
                        .show()
                }

            },
            ContextCompat.getMainExecutor(this)
        )
    }

    private fun takePhoto() {
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    exc.printStackTrace()
                    Toast.makeText(baseContext, exc.message, Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val result = Intent()
                    result.putExtra("TYPE", "SELFIE")
                    result.data = Uri.parse(photoFile.path)
                    setResult(RESULT_OK, result)
                    finish()
                }
            })
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "Pictures").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }
}