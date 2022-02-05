package id.beken.ui.camera

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup

class SelfieFrame : ViewGroup {
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
        val margin = 64
        val eraser = Paint()
        val radius = (width / 2).toFloat() - margin
        val cy = (height / 2).toFloat() - margin
        val cx = radius + margin

        eraser.isAntiAlias = true
        eraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        val stroke = Paint()
        stroke.isAntiAlias = true
        stroke.strokeWidth = 4f
        stroke.color = Color.WHITE
        stroke.style = Paint.Style.STROKE

        canvas.drawCircle(cx, cy, radius, stroke)
        canvas.drawCircle(cx, cy, radius, eraser)
    }
}