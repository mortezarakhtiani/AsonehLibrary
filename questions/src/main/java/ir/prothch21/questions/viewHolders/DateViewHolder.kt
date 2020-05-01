package ir.prothch21.questions.viewHolders

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ir.huri.jcal.JalaliCalendar
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.time.TimePickerDialog
import ir.protech21.persian.utils.PersianCalendar
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_date.view.*
import org.json.JSONArray
import org.json.JSONObject

class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val textViewAsk = itemView.textViewAsk
    val description = itemView.description
    val date = itemView.date
    var now: JalaliCalendar? = null
    lateinit var timePickerDialog:TimePickerDialog
    @SuppressLint("SetTextI18n")
    fun bind(objects: JSONObject) {
        this.json = objects
        textViewAsk.text = objects.getString("question")
        description.text = objects.getString("description")
        date.setOnClickListener {
            if (objects.getString("question_type") == "13") {
                timePickerDialog = TimePickerDialog(itemView.context, { view13, hourOfDay, minute ->
                    date.text = "$hourOfDay : $minute"
                }, 0, 0, true)
                timePickerDialog.setTitle("ساعت شروع را انتخاب کنید")

                timePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(itemView.context.resources.getColor(R.color.colorPrimary)))
                timePickerDialog.show()
                timePickerDialog.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(itemView.context.resources.getColor(R.color.colorgold))
                timePickerDialog.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(itemView.context.resources.getColor(R.color.colorgold))
            } else {
                now = JalaliCalendar()
                val datePickerDialog = DatePickerDialog
                        .newInstance({ view12, year, monthOfYear, dayOfMonth ->
                            now?.set(year, monthOfYear + 1, dayOfMonth)
                            date.text = now?.dayOfWeekDayMonthString + " " + now?.year
                        }, now!!.year, now!!.month - 1, now!!.day)

                datePickerDialog.isThemeDark = true
                val persianCalendar = PersianCalendar()
                persianCalendar.setPersianDate(now!!.year, now!!.month - 1, now!!.day)
                datePickerDialog.show((itemView.context as Activity).fragmentManager, "tpd")
            }
        }
        if (objects.getString("question_type") == "13") {
            date.hint = "زمان مورد نظر خود را انتخاب کنید"
        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", json.getString("question_type").toInt())
        jsonObject.put("question", json.getString("question"))
        val js = JSONObject()
        if (json.getString("question_type") == "8"){
            js.put("year", now?.year)
            js.put("month", now?.month)
            js.put("day", now?.day)
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                js.put("hour", timePickerDialog.timePicker.hour)
                js.put("minute", timePickerDialog.timePicker.minute)
            }
        }
        jsonObject.put("answer", js)
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }
}