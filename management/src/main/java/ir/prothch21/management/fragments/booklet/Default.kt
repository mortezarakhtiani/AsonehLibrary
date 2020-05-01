package ir.prothch21.management.fragments.booklet

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import ir.huri.jcal.JalaliCalendar
import ir.protech21.bottomsheet.BottomSheetFragment
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.time.TimePickerDialog
import ir.prothch21.management.BookletAdd
import ir.prothch21.management.R
import kotlinx.android.synthetic.main.custom_booklet2.view.*

class Default : Fragment() {

    var bookletAdd: BookletAdd? = null
    var timePickerDialog: TimePickerDialog? = null
    var now: JalaliCalendar? = null
    var spinner2BottomSheet: BottomSheetFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.custom_booklet2, container, false)
        bookletAdd = BookletAdd()
        view.calender!!.setOnClickListener { v: View ->
            now = JalaliCalendar()
            val datePickerDialog = DatePickerDialog
                    .newInstance({ view12: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                        now!![year, monthOfYear + 1] = dayOfMonth
                        (v as TextView).text = String.format("%s %s %s %s", now!!.dayOfWeekString, now!!.day, now!!.monthString, now!!.year)
                    }, now!!.year, now!!.month - 1, now!!.day)
            datePickerDialog.isThemeDark = true
            datePickerDialog.show(activity!!.fragmentManager, "tpd")
        }
        view. clock.setOnClickListener(View.OnClickListener { v: View? ->
            timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view1: TimePicker?, hourOfDay: Int, minute: Int -> view.clock.text = String.format("%d:%d", hourOfDay, minute) }, 0, 0, true)
            timePickerDialog!!.window!!.setBackgroundDrawable(ColorDrawable(activity!!.resources.getColor(R.color.colorPrimary)))
            timePickerDialog!!.show()
            timePickerDialog!!.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(context!!.resources.getColor(R.color.colorgold))
            timePickerDialog!!.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(context!!.resources.getColor(R.color.colorgold))
        })
        val list = arrayOf("هر هفته", "هر دو هفته", "هرماه", "هر دو ماه", "هر سه ماه", "هر چهار ماه", "هر پنج ماه", "هر شش ماه")
        view.spinner2.setOnClickListener(View.OnClickListener { v: View? ->
            spinner2BottomSheet = BottomSheetFragment.newInstance(list.toCollection(ArrayList()),
                    "دوره تکرار هر قسط را وارد کنید") { view.spinner2.text = list[it] }
            assert(fragmentManager != null)
            spinner2BottomSheet?.show(fragmentManager!!, "priod")
        })
        return view
    }

}