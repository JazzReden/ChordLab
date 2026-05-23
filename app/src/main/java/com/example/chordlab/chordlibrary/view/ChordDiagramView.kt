package com.example.chordlab.chordlibrary.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.chordlab.chordlibrary.model.FretMarker

class ChordDiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8A8175")
        strokeWidth = 3f
    }
    private val nutPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#40362E")
        strokeWidth = 9f
    }
    private val markerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#129486")
    }
    private val topLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4F5F5B")
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }
    private val bottomLabelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#14565A")
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }
    private val fretNumberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#14565A")
        textSize = 22f
        textAlign = Paint.Align.RIGHT
    }
    private val startFretPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#14565A")
        textSize = 24f
        textAlign = Paint.Align.RIGHT
        isFakeBoldText = true
    }

    private var markers: List<FretMarker> = emptyList()
    private var stringIndicators: List<String> = emptyList()
    private var bottomStringLabels: List<String> = emptyList()
    private var barreFret: Int? = null
    private var startingFret: Int = 0

    fun setChord(
        markers: List<FretMarker>,
        stringIndicators: List<String>,
        barreFret: Int?,
        startingFret: Int,
        bottomStringLabels: List<String> = emptyList()
    ) {
        this.markers = markers
        this.stringIndicators = stringIndicators
        this.bottomStringLabels = bottomStringLabels
        this.barreFret = barreFret
        this.startingFret = startingFret
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val left = width * 0.22f
        val right = width * 0.86f
        val top = height * 0.28f
        val bottom = height * 0.78f
        val stringGap = (right - left) / 5f
        val fretGap = (bottom - top) / 4f
        val fretCount = 4

        val startLabel = if (startingFret == 0) "Open" else "${startingFret}fr"
        canvas.drawText(startLabel, left - 14f, top - 42f, startFretPaint)

        for (i in 0..5) {
            val x = left + stringGap * i
            canvas.drawLine(x, top, x, bottom, linePaint)

            stringIndicators.getOrNull(i)?.takeIf { it.isNotBlank() }?.let { label ->
                canvas.drawText(label, x, top - 24f, topLabelPaint)
            }

            bottomStringLabels.getOrNull(i)?.takeIf { it.isNotBlank() }?.let { label ->
                canvas.drawText(label, x, bottom + 28f, bottomLabelPaint)
            }
        }

        for (i in 0..fretCount) {
            val y = top + fretGap * i
            canvas.drawLine(left, y, right, y, if (i == 0 && startingFret == 0) nutPaint else linePaint)

            if (i < fretCount) {
                val absoluteFret = if (startingFret == 0) i + 1 else startingFret + i
                canvas.drawText(
                    absoluteFret.toString(),
                    left - 18f,
                    y + fretGap * 0.58f,
                    fretNumberPaint
                )
            }
        }

        barreFret?.let { relativeFret ->
            val y = top + fretGap * (relativeFret.coerceIn(1, fretCount) - 0.5f)
            canvas.drawRoundRect(left - 8f, y - 18f, right + 8f, y + 18f, 18f, 18f, markerPaint)
        }

        markers.forEach { marker ->
            val stringIndex = (marker.stringNumber - 1).coerceIn(0, 5)
            val x = left + stringGap * stringIndex
            val y = markerY(marker.fret, top, fretGap, startingFret)
            canvas.drawCircle(x, y, 18f, markerPaint)
        }
    }

    private fun markerY(relativeFret: Int, top: Float, fretGap: Float, startingFret: Int): Float {
        return if (relativeFret <= 0 && startingFret == 0) {
            top - 10f
        } else {
            val clamped = relativeFret.coerceIn(1, 4)
            top + fretGap * (clamped - 0.5f)
        }
    }
}
