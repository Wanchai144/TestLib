package com.example.testlibs.persentation.ui.custom

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.SurfaceTexture
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.TextureView

class VideoViewWithWatermark @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 40f
        typeface = Typeface.DEFAULT_BOLD
        isAntiAlias = true
    }
    var watermarkText: String = "Sample Watermark"

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Initialize your streaming setup here
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Redraw or adjust things when surface dimensions change
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Clean up anything that needs to be released
    }

    fun drawWatermark() {
        val canvas = holder.lockCanvas()
        try {
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR) // Clear previous drawings
                canvas.drawText(watermarkText, 10f, height - 10f, textPaint)
            }
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
