package ir.prothch21.management.fragments.booklet

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ir.protech21.bottomsheet.BottomSheetFragment
import ir.prothch21.management.BookletAdd
import ir.prothch21.management.R
import kotlinx.android.synthetic.main.fragment_booklet.*
import kotlinx.android.synthetic.main.fragment_booklet.view.*

/**
 * A simple [Fragment] subclass.
 */
class Booklet : Fragment() {
    var phone: String? = ""
    var token: String? = ""
    var bookletAdd: BookletAdd? = null
    var spinnerBottomSheetFragment: BottomSheetFragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booklet, container, false)
        bookletAdd = activity as BookletAdd?
        var textView = bookletAdd!!.linearLayout?.getChildAt(1) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.colorblue))
        textView = bookletAdd!!.linearLayout?.getChildAt(0) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.gray))
        view.previous.setOnClickListener { activity?.finish() }
        view.next.setOnClickListener(this::next)
        view.sms.setOnCheckedChangeListener { buttonView, isChecked: Boolean -> phone = if (isChecked) activity!!.getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("phone", null) else "" }
        view.notify.setOnCheckedChangeListener { buttonView, isChecked -> token = if (isChecked) activity!!.getSharedPreferences("Profile", Context.MODE_PRIVATE).getString("token", null) else "" }
        val list = arrayOf("وام خانگی", "بیمه", "لیرینگ", "شهریه", "خیریه", "کرایه", "وام", "سایر")
        view.spinner!!.setOnClickListener { v: View? ->
            spinnerBottomSheetFragment = BottomSheetFragment.newInstance(list.toCollection(ArrayList()), "نوع اقسط را مشخص کنید") {spinner!!.text = list[it] }
            assert(fragmentManager != null)
            spinnerBottomSheetFragment?.show(fragmentManager!!, "type")
        }
        return view
    }

    fun allow(): Boolean {
        if (spinnerBottomSheetFragment == null) {
            return false
        }
        return !(spinnerBottomSheetFragment!!.selectedItemPosition == -1 || TextUtils.isEmpty(recaiver!!.text) || TextUtils.isEmpty(numberPay!!.text))
    }


    private fun next(view: View) {
        if (allow()) {
            val fragmentTransaction = fragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.detailsFrameLayout, bookletAdd!!.booklet2!!)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            Toast.makeText(context, "لطفا تمام فیلد ها را کامل کنید", Toast.LENGTH_SHORT).show()
        }
    }
}