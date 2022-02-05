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
import androidx.lifecycle.MutableLiveData
import id.beken.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class IdCardCameraActivity : AppCompatActivity() {

    private val flashMode = MutableLiveData(false)
    private lateinit var outputDirectory: File
    private var imageCapture: ImageCapture = ImageCapture.Builder()
        .setTargetResolution(Size(540, 420))
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_id_card_camera)

        startCamera()
        outputDirectory = getOutputDirectory()
        val cameraBack = findViewById<ImageView>(R.id.btn_take_id_card)
        val cameraFlash = findViewById<ImageView>(R.id.flash_id_card)
        cameraBack.setOnClickListener {
            takePhoto()
        }
        cameraFlash.setOnClickListener {
            if (imageCapture.flashMode == ImageCapture.FLASH_MODE_ON) {
                cameraFlash.setImageResource(R.drawable.ic_flash_off)
                imageCapture.flashMode = ImageCapture.FLASH_MODE_OFF
                flashMode.value = false
            } else {
                cameraFlash.setImageResource(R.drawable.ic_flash)
                imageCapture.flashMode = ImageCapture.FLASH_MODE_ON
                flashMode.value = true
            }
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val viewFinder = findViewById<PreviewView>(R.id.id_card_camera)
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
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
                    val camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
                    flashMode.observe(this, {
                        camera.cameraControl.enableTorch(it)
                    })
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
                    result.putExtra("TYPE", "IDCARD")
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