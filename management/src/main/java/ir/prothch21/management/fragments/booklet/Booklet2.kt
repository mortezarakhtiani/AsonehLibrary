package ir.prothch21.management.fragments.booklet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import co.ceryle.radiorealbutton.RadioRealButton
import ir.protech21.utils.Utils.Companion.LoadFragment
import ir.prothch21.management.BookletAdd
import ir.prothch21.management.R
import ir.prothch21.management.bottomsheet.BottomSheetFragmentAdd
import kotlinx.android.synthetic.main.fragment_booklet2.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("ValidFragment")
class Booklet2 : Fragment() {
    var bookletPriceModels = ArrayList<BookletPriceModel>()
    lateinit var custom: Custom

    private var bookletAdd: BookletAdd? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_booklet2, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bookletAdd = activity as BookletAdd
        custom = Custom()
        val defaul = Default()
        card.setOnClickListener {  (activity as BookletAdd).save(it,custom,defaul) }
        var textView = bookletAdd!!.linearLayout?.getChildAt(0) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.colorblue))
        textView = bookletAdd!!.linearLayout?.getChildAt(1) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.gray))
        add.hide()
        radioRealButtonGroup.setOnPositionChangedListener { button: RadioRealButton?, currentPosition: Int, lastPosition: Int ->
            bookletPriceModels = ArrayList()
            if (currentPosition == 0){
                LoadFragment(childFragmentManager, custom, R.id.frameLayout)
                add.show()
            }else{
                LoadFragment(childFragmentManager, defaul, R.id.frameLayout)
                add.hide()
            }

        }
        add.setOnClickListener {
            val fragmentAdd = BottomSheetFragmentAdd.newInstance { string, jalaliCalendar, timePicker ->
                custom.models!!.add(Custom.Model(jalaliCalendar!!.day, jalaliCalendar.month, jalaliCalendar.year, timePicker.currentHour, timePicker.currentMinute, Integer.parseInt(string)))
                custom.adapter!!.notifyDataSetChanged()
            }
            fragmentAdd.show(bookletAdd?.supportFragmentManager!!, "aqsat");
        }
        LoadFragment(childFragmentManager, defaul, R.id.frameLayout)
    }



}