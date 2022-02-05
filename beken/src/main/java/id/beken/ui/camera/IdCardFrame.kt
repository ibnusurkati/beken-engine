package id.beken.ui.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup

class IdCardFrame : ViewGroup {
    constructor(context: Context?) : super(context) {}

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int = 0) : super(
        context,
        attrs,
        defStyle
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    public override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {}
    override fun shouldDelayChildPressedState(): Boolean {
        return false
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val radius = 8.toFloat()
        val margin = 32
        val left = margin.toFloat()
        val top = ((height / 2) - 150).toFloat()
        val right = (width - margin).toFloat()
        val bottom = ((top + right) * 0.75).toFloat()
        val frame = RectF(left, top, right, bottom)
        val eraser = Paint()
        val stroke = Paint()

        eraser.isAntiAlias = true
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        stroke.isAntiAlias = true
        stroke.strokeWidth = 4f
        stroke.color = Color.WHITE
        stroke.style = Paint.Style.STROKE

        canvas.drawRoundRect(frame, radius, radius, eraser)
        canvas.drawRoundRect(frame, radius, radius, stroke)
    }
}