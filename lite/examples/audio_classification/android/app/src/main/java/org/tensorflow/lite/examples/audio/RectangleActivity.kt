package org.tensorflow.lite.examples.audio

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class RectangleActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rectangle_activity)
        val imageView = findViewById<MyImageView>(R.id.imageView)
        imageView.apply {
            setImageResource(R.drawable.de_thi)
            addRectangle(100f, 100f, 200f, 200f)
            addRectangle(60f, 300f, 500f, 400f)
            addRectangle(150f, 500f, 650f, 750f)
        }

        val tv1 = findViewById<TextView>(R.id.tvRectangle1)
        val tv2 = findViewById<TextView>(R.id.tvRectangle2)
        val tv3 = findViewById<TextView>(R.id.tvRectangle3)
        tv1.setOnClickListener {
            imageView.selectRect(0)
            it.setBackgroundResource(R.color.background_yellow)
            tv2.setBackgroundResource(R.color.transparent)
            tv3.setBackgroundResource(R.color.transparent)
        }
        tv2.setOnClickListener {
            imageView.selectRect(1)
            it.setBackgroundResource(R.color.background_yellow)
            tv1.setBackgroundResource(R.color.transparent)
            tv3.setBackgroundResource(R.color.transparent)
        }
        tv3.setOnClickListener {
            imageView.selectRect(2)
            it.setBackgroundResource(R.color.background_yellow)
            tv1.setBackgroundResource(R.color.transparent)
            tv2.setBackgroundResource(R.color.transparent)
        }
    }
}

class MyImageView constructor(
    ctx: Context,
    attrs: AttributeSet?
) : AppCompatImageView(ctx, attrs) {

    private var listRectangle: MutableList<RecData> = mutableListOf()
    private val paintYellow by lazy {
        Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.STROKE
            strokeWidth = 7f
        }
    }
    private val paintBlack by lazy {
        Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL
            strokeWidth = 5f
        }
    }
    private val paintWhite by lazy {
        Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            strokeWidth = 2f
            textSize = lengthCorner
            textAlign = Paint.Align.CENTER
        }
    }
    private val paintGray by lazy {
        Paint().apply {
            color = Color.parseColor("#99000000")
            style = Paint.Style.FILL
        }
    }
    private val paintLightGray by lazy {
        Paint().apply {
            color = Color.parseColor("#4D000000")
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 7f
        }
    }
    private var lengthCorner = 25f
    private val boundText: Rect = Rect()
    private var path = Path()
    private var pathBig = Path()
    private var currentIndex = 0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        path.reset()
        pathBig.reset()

        for (index in listRectangle.indices) {
            val rec = listRectangle[index]
            path.addRect(
                rec.left - paintYellow.strokeWidth / 2,
                rec.top - paintYellow.strokeWidth / 2,
                rec.right + paintYellow.strokeWidth / 2,
                rec.bottom + paintYellow.strokeWidth / 2,
                Path.Direction.CW
            )
            canvas?.apply {
                if (index != currentIndex) {
                    drawRect(rec.left, rec.top, rec.right, rec.bottom, paintLightGray)
                    drawCircle((rec.left+rec.right)/2, (rec.top+rec.bottom)/2, lengthCorner, paintBlack)

                    paintWhite.getTextBounds("$index", 0, "$index".length, boundText)
                    drawText("$index", (rec.left+rec.right)/2, (rec.top+rec.bottom)/2 + boundText.height()/2, paintWhite)
                } else {
                    drawLine(rec.left, rec.top, rec.left + lengthCorner, rec.top, paintYellow)
                    drawLine(rec.left, rec.top-paintYellow.strokeWidth/2, rec.left, rec.top + lengthCorner, paintYellow)
                    drawLine(rec.right - lengthCorner, rec.top, rec.right, rec.top, paintYellow)
                    drawLine(rec.right, rec.top-paintYellow.strokeWidth/2, rec.right, rec.top + lengthCorner, paintYellow)
                    drawLine(rec.left, rec.bottom - lengthCorner, rec.left, rec.bottom+paintYellow.strokeWidth/2, paintYellow)
                    drawLine(rec.left, rec.bottom, rec.left + lengthCorner, rec.bottom, paintYellow)
                    drawLine(rec.right - lengthCorner, rec.bottom, rec.right, rec.bottom, paintYellow)
                    drawLine(rec.right, rec.bottom - lengthCorner, rec.right, rec.bottom+paintYellow.strokeWidth/2, paintYellow)
                }
            }
        }

        // draw where not in the path
        pathBig.addRect(0f, 0f, width.toFloat(), height.toFloat(), Path.Direction.CW)
        pathBig.op(path, Path.Op.DIFFERENCE)
        canvas?.apply {
            drawPath(pathBig, paintGray)
        }
    }

    fun addRectangle(left: Float, top: Float, right: Float, bottom: Float) {
        listRectangle.add(RecData(left, top, right, bottom))
        invalidate()
    }

    fun selectRect(index: Int) {
        currentIndex = index
        invalidate()
    }

    data class RecData(val left: Float, val top: Float, val right: Float, val bottom: Float)
}
