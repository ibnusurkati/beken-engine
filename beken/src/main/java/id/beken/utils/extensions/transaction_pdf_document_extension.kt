package id.beken.utils.extensions

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import id.beken.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

fun PdfDocument.share(context: Context, prefixFilename: String) {

    try {
        val uri = this.download(context, prefixFilename)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "application/pdf"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Membagikan Struk")
        context.startActivity(shareIntent)
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, R.string.failed_share_file, Toast.LENGTH_SHORT).show()
    }
}

fun PdfDocument.download(context: Context, prefixFilename: String) : Uri? {
    val currentTime: String = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
    val fileName = "$prefixFilename - $currentTime.pdf"

    var documentUri: Uri? = null
    var fos: OutputStream? = null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        context.contentResolver?.also { resolver ->

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            documentUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            fos = documentUri?.let { resolver.openOutputStream(it) }
        }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val image = File(imagesDir, fileName)
        documentUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", image)
        fos = FileOutputStream(image)
    }

    fos?.use {
        this.writeTo(it)
    }

    return documentUri
}