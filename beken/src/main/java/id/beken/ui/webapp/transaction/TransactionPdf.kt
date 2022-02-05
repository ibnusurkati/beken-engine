package id.beken.ui.webapp.transaction

import android.content.Context
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.text.TextPaint
import androidx.core.content.ContextCompat
import id.beken.R
import id.beken.models.TransactionOutput
import id.beken.utils.extensions.drawMultilineText

class TransactionPdf {
    companion object {
        fun build(context: Context, content: CharSequence) : PdfDocument {
            val text = TextPaint()
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(350, 500, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            text.typeface = Typeface.MONOSPACE
            text.textSize = 12F
            text.color = ContextCompat.getColor(context, R.color.primary_color)

            canvas.drawMultilineText(content, text, canvas.width - 40, 20F, 20F)

            pdfDocument.finishPage(page)
            return pdfDocument
        }
    }

}