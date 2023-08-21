package org.tensorflow.lite.examples.audio

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarActivity : AppCompatActivity() {
    private val adapter = MyAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val data = mockData()
        adapter.addItems(data.data?: listOf())

        findViewById<TextView>(R.id.tvCalendarBig).text = data.roomName
        findViewById<TextView>(R.id.tvCalendarSmall).text = data.timeRange
    }
}



private fun mockData(): Room {
    return Room(
        "Phòng họp tầng 8",
        "01/08/2023",
        listOf(
            Day("Thứ 2",
                listOf(
                    Event("Namnv", "08:00:00", "10:00:00", "Test lịch họp"),
                    Event("Namnv", "11:00:00", "12:00:00", "Test lịch họp"),
                    Event("HiepUH", "13:30:00", "14:30:00", "Test lịch họp"),
                    Event("HiepUH", "15:00:00", "16:00:00", "Test lịch họp"),
                )
            ),
            Day("Thứ 3",
                listOf(
                    Event("Namnv", "09:30:00", "10:30:00", "Test lịch họp"),
                    Event("HiepUH", "16:00:00", "17:30:00", "Test lịch họp"),
                )
            )
        )
    )
}

data class Room(var roomName: String? = null, var timeRange: String? = null, val data: List<Day>? = null)
data class Day(var dayName: String? = null, var events: List<Event>? = null)

data class Event(
    var user: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val content: String? = null
)

class MyAdapter : RecyclerView.Adapter<MyAdapter.MyVH>() {
    companion object {
        const val TIME_LINE = 0
        const val DAY = 1
    }

    private val dataList: MutableList<Any> = mutableListOf(true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return if (viewType == 0) {
            TimeLineVH(
                LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
            )
        } else {
            DayVH(
                LayoutInflater.from(parent.context).inflate(R.layout.day_item, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TIME_LINE
        } else {
            DAY
        }
    }

    fun addItems(data: List<Day>) {
        dataList.addAll(data)
        notifyItemRangeChanged(1, data.size)
    }

    class TimeLineVH(itemView: View) : MyVH(itemView) {
        override fun onBind(data: Any) {
            val root = itemView.findViewById<ViewGroup>(R.id.rootTimeLine)

            itemView.findViewById<TextView>(R.id.tvTimeline1).apply {
                val param = (layoutParams as MarginLayoutParams)
                param.topMargin = 100 - 30
                layoutParams = param
            }
            itemView.findViewById<TextView>(R.id.tvTimeline2).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 1 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline3).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 2 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline4).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 3 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline5).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 4 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline6).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 5 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline7).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 6 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline8).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 7 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline9).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 8 - 30
                    layoutParams = param
                }
            }
            itemView.findViewById<TextView>(R.id.tvTimeline10).apply {
                post {
                    val param = (layoutParams as MarginLayoutParams)
                    param.topMargin = 100 + (root.height - 100) / 10 * 9 - 30
                    layoutParams = param
                }
            }
        }
    }

    class DayVH(itemView: View) : MyVH(itemView) {
        override fun onBind(data: Any) {
            data as Day
            val textView = itemView.findViewById<TextView>(R.id.textView)
            textView.text = data.dayName
            textView.post {
                textView.height = 100
            }

            val dayView = itemView.findViewById<DayView>(R.id.dayView)
            dayView.drawEvent(data.events ?: listOf())
        }
    }

    open class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun onBind(data: Any) {}
    }
}

class DayView constructor(
    ctx: Context,
    attrs: AttributeSet?
) : View(ctx, attrs) {
    private var listEvent: List<Event> = listOf()
    private val paint by lazy {
        Paint().apply {
            color = Color.YELLOW
            style = Paint.Style.FILL
            strokeWidth = 5f
        }
    }

    private val textPaint by lazy {
        Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            strokeWidth = 2f
            textSize = 40f
            textAlign = Paint.Align.CENTER
        }
    }
    private val colorList = listOf(
        Color.YELLOW,
        Color.BLUE,
        Color.GREEN,
        Color.RED,
        Color.CYAN,
        Color.MAGENTA,
        Color.GRAY,
        Color.DKGRAY,
        Color.LTGRAY,
    ).shuffled()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (index in listEvent.indices) {
            val event = listEvent[index]
            val startTime = (event.startTime?.split(":")?.firstOrNull() ?: "0").toInt()
            val endTime = (event.endTime?.split(":")?.firstOrNull() ?: "0").toInt()
            val startY = height / 10 * (startTime - 8)
            val endY = height / 10 * (endTime - 8)
            canvas?.apply {
                drawRect(0f, startY.toFloat(), width.toFloat(), endY.toFloat(), paint.apply { color = colorList[index % colorList.size]})
                drawText("${event.user}", width / 2f, startY + 60f,  textPaint)
                drawText("${event.content}", width / 2f, startY + 100f,  textPaint)
            }
        }
    }

    fun drawEvent(events: List<Event>) {
        listEvent = events
        invalidate()
    }
}
