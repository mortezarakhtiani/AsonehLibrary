package ir.prothch21.management.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.huri.jcal.JalaliCalendar
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.time.TimePickerDialog
import ir.prothch21.management.R
import java.util.*

class BottomSheetFragmentAdd : BottomSheetDialogFragment() {
    private var Price: EditText? = null
    private var date: TextView? = null
    private var sabt: Button? = null
    var jalaliCalendar: JalaliCalendar? = null
    private var time: TextView? = null
    var timePickerDialog: TimePickerDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.add_aqsat, null)
        dialog.setContentView(view)
        initView(dialog)
        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)
        date!!.setOnClickListener { view: View -> date(view) }
        time!!.setOnClickListener { view: View -> time(view) }
        sabt!!.setOnClickListener { v: View? ->
            if (Price!!.text.toString().isEmpty()) {
                Toast.makeText(activity, "لطفا قیمت را وارد کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (timePickerDialog == null) {
                Toast.makeText(activity, "لطفا ساعت را وارد کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            function?.let { it(Price!!.text.toString(), jalaliCalendar, timePickerDialog!!.timePicker) }
            dismiss()
        }
    }

    private fun time(view: View) {
        timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { view13: TimePicker?, hourOfDay: Int, minute: Int -> (view as TextView).text = String.format("%s : %s", hourOfDay.toString(), minute.toString()) }, 0, 0, true)
        timePickerDialog!!.setTitle("ساعت شروع را انتخاب کنید")
        timePickerDialog!!.window!!.setBackgroundDrawable(ColorDrawable(activity!!.resources.getColor(R.color.colorPrimary)))
        timePickerDialog!!.show()
        timePickerDialog!!.getButton(TimePickerDialog.BUTTON_POSITIVE).setTextColor(context!!.resources.getColor(R.color.colorgold))
        timePickerDialog!!.getButton(TimePickerDialog.BUTTON_NEGATIVE).setTextColor(context!!.resources.getColor(R.color.colorgold))
    }

    private fun date(view: View) {
        jalaliCalendar = JalaliCalendar()
        val datePickerDialog = DatePickerDialog
                .newInstance({ view1: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    jalaliCalendar!![year, monthOfYear + 1] = dayOfMonth
                    //
                    date!!.text = String.format("%s %s %s %s", jalaliCalendar!!.dayOfWeekString, jalaliCalendar!!.day, jalaliCalendar!!.monthString, jalaliCalendar!!.year)
                }, jalaliCalendar!!.year, jalaliCalendar!!.month - 1, jalaliCalendar!!.day)
        datePickerDialog.isThemeDark = true
        assert(this.fragmentManager != null)
        datePickerDialog.show(activity?.fragmentManager, "tpd")
    }

    private fun initView(dialog: Dialog) {
        Price = dialog.findViewById(R.id.Price)
        date = dialog.findViewById(R.id.date)
        sabt = dialog.findViewById(R.id.sabt)
        time = dialog.findViewById(R.id.time)
    }

    companion object {
        private var function:((String,JalaliCalendar?,TimePicker)->Unit)? = null
        fun newInstance(function: (String,JalaliCalendar?,TimePicker)->Unit): BottomSheetFragmentAdd {
            Companion.function=function
            return BottomSheetFragmentAdd()
        }
    }
}