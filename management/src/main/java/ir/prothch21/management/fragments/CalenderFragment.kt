package ir.prothch21.management.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.utils.PersianCalendar
import ir.prothch21.management.R

@SuppressLint("ValidFragment")
class CalenderFragment : Fragment {
    var textView: TextView? = null
    var string: String? = null

    constructor(s: String?) {
        string = s
    }

    constructor() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.calendar_fragment, container, false)
        textView = view.findViewById(R.id.calender)
        if (string != null) {
            textView?.setText(string)
        }
        view.findViewById<View>(R.id.cardView).setOnClickListener {
            val now = PersianCalendar()
            val datePickerDialog = DatePickerDialog
                    .newInstance({ view, year, monthOfYear, dayOfMonth -> textView?.setText(String.format("%s/%s/%s", year, monthOfYear + 1, dayOfMonth)) }, now.persianYear, now.persianMonth, now.persianDay)
            datePickerDialog.isThemeDark = true
            datePickerDialog.show(activity!!.fragmentManager, "tpd")
            //        DatePicker  datePicker = view.findViewById(R.id.calender);
//        datePicker.init(now.getPersianYear(), now.getPersianMonth(), now.getPersianDay(), new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Toast.makeText(getActivity(), String.valueOf(year)+":"+String.valueOf(monthOfYear), Toast.LENGTH_SHORT).show();
        }
        return view
    }
}