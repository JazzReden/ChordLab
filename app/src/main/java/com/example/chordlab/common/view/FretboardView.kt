package com.example.chordlab.common.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.chordlab.common.model.FretboardPosition

class FretboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val woodPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5C4033")
    }
    private val stringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2B2118")
        strokeWidth = 1.4f
        strokeCap = Paint.Cap.ROUND
    }
    private val fretWirePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C9B896")
        strokeWidth = 3.5f
    }
    private val nutPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F2E8D5")
        strokeWidth = 10f
    }
    private val inlayPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#8D7B63")
    }
    private val noteTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val fretNumberPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#14565A")
        textAlign = Paint.Align.CENTER
        textSize = 26f
    }
    private val scaleTitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#14565A")
        textAlign = Paint.Align.CENTER
        textSize = 28f
        isFakeBoldText = true
    }
    private val rootBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F59E0B")
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private var positions: List<FretboardPosition> = emptyList()
    private var startFret = 0
    private var fretCount = 5
    private var scaleNotesLabel: String = ""

    fun setFretboardData(
        positions: List<FretboardPosition>,
        startFret: Int,
        fretCount: Int,
        scaleNotesLabel: String = ""
    ) {
        this.positions = positions
        this.startFret = startFret
        this.fretCount = fretCount.coerceAtLeast(1)
        this.scaleNotesLabel = scaleNotesLabel
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val boardLeft = width * 0.06f
        val boardRight = width * 0.94f
        val boardTop = height * 0.22f
        val boardBottom = height * 0.72f
        val numbersBaseline = height * 0.84f
        val boardRect = RectF(boardLeft, boardTop, boardRight, boardBottom)

        val stringInset = (boardBottom - boardTop) * 0.1f
        val stringTop = boardTop + stringInset
        val stringBottom = boardBottom - stringInset
        val stringSpacing = (stringBottom - stringTop) / 5f
        val fretGap = (boardRight - boardLeft) / fretCount

        canvas.drawRect(boardRect, woodPaint)

        if (scaleNotesLabel.isNotBlank()) {
            canvas.drawText(scaleNotesLabel, width / 2f, height * 0.1f, scaleTitlePaint)
        }

        val saved = canvas.save()
        canvas.clipRect(boardRect)

        canvas.drawLine(boardLeft, stringTop, boardLeft, stringBottom, nutPaint)

        for (fretIndex in 1..fretCount) {
            val x = boardLeft + fretGap * fretIndex
            canvas.drawLine(x, stringTop, x, stringBottom, fretWirePaint)
        }

        drawInlays(canvas, boardLeft, stringTop, stringBottom, fretGap)

        for (stringNumber in 1..6) {
            val y = stringY(stringNumber, stringTop, stringSpacing)
            canvas.drawLine(boardLeft, y, boardRight, y, stringPaint)
        }

        val markerRadius = (stringSpacing * 0.36f).coerceAtMost(fretGap * 0.32f)
        noteTextPaint.textSize = markerRadius * 1.1f

        positions.forEach { position ->
            val centerX = fretCenterX(position.fret, boardLeft, fretGap)
            val centerY = stringY(position.stringNumber, stringTop, stringSpacing)
            val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = noteColor(position.noteName)
            }

            if (position.isRoot) {
                val half = markerRadius + 2f
                canvas.drawRoundRect(
                    centerX - half,
                    centerY - half,
                    centerX + half,
                    centerY + half,
                    6f,
                    6f,
                    fillPaint
                )
                canvas.drawRoundRect(
                    centerX - half,
                    centerY - half,
                    centerX + half,
                    centerY + half,
                    6f,
                    6f,
                    rootBorderPaint
                )
            } else {
                canvas.drawCircle(centerX, centerY, markerRadius, fillPaint)
            }

            val displayNote = formatNoteLabel(position.noteName)
            canvas.drawText(
                displayNote,
                centerX,
                centerY - (noteTextPaint.descent() + noteTextPaint.ascent()) / 2f,
                noteTextPaint
            )
        }

        for (stringNumber in 1..6) {
            val y = stringY(stringNumber, stringTop, stringSpacing)
            canvas.drawLine(boardLeft, y, boardRight, y, stringPaint)
        }

        canvas.restoreToCount(saved)

        for (spaceIndex in 0 until fretCount) {
            val absoluteFret = startFret + spaceIndex
            val label = if (absoluteFret == 0) "0" else absoluteFret.toString()
            val centerX = boardLeft + fretGap * (spaceIndex + 0.5f)
            canvas.drawText(label, centerX, numbersBaseline, fretNumberPaint)
        }
    }

    private fun stringY(stringNumber: Int, stringTop: Float, stringSpacing: Float): Float {
        val index = (stringNumber - 1).coerceIn(0, 5)
        return stringTop + stringSpacing * index
    }

    private fun fretCenterX(absoluteFret: Int, boardLeft: Float, fretGap: Float): Float {
        return if (absoluteFret == 0) {
            boardLeft + fretGap * 0.22f
        } else {
            boardLeft + fretGap * (absoluteFret - startFret + 0.5f)
        }
    }

    private fun drawInlays(
        canvas: Canvas,
        boardLeft: Float,
        stringTop: Float,
        stringBottom: Float,
        fretGap: Float
    ) {
        val centerY = (stringTop + stringBottom) / 2f
        val inlayRadius = fretGap * 0.1f

        for (absoluteFret in listOf(3, 5, 7, 9, 12)) {
            if (absoluteFret < startFret || absoluteFret > startFret + fretCount) continue
            val x = fretCenterX(absoluteFret, boardLeft, fretGap)
            canvas.drawCircle(x, centerY, inlayRadius, inlayPaint)
            if (absoluteFret == 12) {
                canvas.drawCircle(x - inlayRadius * 1.6f, centerY, inlayRadius, inlayPaint)
                canvas.drawCircle(x + inlayRadius * 1.6f, centerY, inlayRadius, inlayPaint)
            }
        }
    }

    private fun noteColor(noteName: String): Int {
        val normalized = noteName.replace("♯", "#")
        return when (normalized) {
            "A" -> Color.parseColor("#F59E0B")
            "A#" -> Color.parseColor("#D97706")
            "B" -> Color.parseColor("#84CC16")
            "C" -> Color.parseColor("#EF4444")
            "C#" -> Color.parseColor("#DC2626")
            "D" -> Color.parseColor("#3B82F6")
            "D#" -> Color.parseColor("#2563EB")
            "E" -> Color.parseColor("#8B5CF6")
            "F" -> Color.parseColor("#EC4899")
            "F#" -> Color.parseColor("#DB2777")
            "G" -> Color.parseColor("#129486")
            "G#" -> Color.parseColor("#0F766E")
            else -> Color.parseColor("#129486")
        }
    }

    private fun formatNoteLabel(noteName: String): String {
        return noteName.replace("#", "♯")
    }
}
