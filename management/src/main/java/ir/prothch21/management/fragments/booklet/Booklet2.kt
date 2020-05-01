package ir.prothch21.management.fragments.booklet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.ceryle.radiorealbutton.RadioRealButton
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import ir.protech21.utils.Utils.Companion.LoadFragment
import ir.prothch21.management.BookletAdd
import ir.prothch21.management.R
import kotlinx.android.synthetic.main.custom_booklet2.*
import kotlinx.android.synthetic.main.fragment_booklet.*
import kotlinx.android.synthetic.main.fragment_booklet2.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
@SuppressLint("ValidFragment")
class Booklet2 : Fragment() {
    var bookletPriceModels = ArrayList<BookletPriceModel>()
    var check = false
    private var bookletAdd: BookletAdd? = null
    var defaul: Default? = null
    var custom: Custom? = null
    private var click = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_booklet2 , container, false)

        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        card.setOnClickListener { view: View -> save(view) }
        bookletAdd = activity as BookletAdd?
        custom = Custom()
        defaul = Default()
        var textView = bookletAdd!!.linearLayout?.getChildAt(0) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.colorblue))
        textView = bookletAdd!!.linearLayout?.getChildAt(1) as android.widget.TextView
        textView.setTextColor(resources.getColor(R.color.gray))
        add.hide()
        LoadFragment(childFragmentManager, defaul!!, R.id.frameLayout)
        radioRealButtonGroup.setOnPositionChangedListener { button: RadioRealButton?, currentPosition: Int, lastPosition: Int ->
            bookletPriceModels = ArrayList()
            if (currentPosition == 0) {
                check = true
                LoadFragment(childFragmentManager, custom!!, R.id.frameLayout)
                add.show()
            } else {
                check = false
                LoadFragment(childFragmentManager, defaul!!, R.id.frameLayout)
                add.hide()
            }
        }
    }


    private fun save(view: View) {
        if (click) {
            return
        }
        click = true
        val jsonArray = JSONArray()
        if (check) {
            for (model in custom?.models!!) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("price", model.price)
                    jsonObject.put("year", model.year)
                    jsonObject.put("month", model.month)
                    jsonObject.put("day", model.day)
                    jsonObject.put("hour", model.hour)
                    jsonObject.put("minute", model.minute)
                    jsonArray.put(jsonObject)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            val jsonObject = JSONObject()
            if (defaul!!.spinner2BottomSheet != null && defaul!!.spinner2BottomSheet?.selectedItemPosition!! > -1 && radioRealButtonGroup!!.position == 1) {
                bookletPriceModels = ArrayList()
                bookletPriceModels.add(BookletPriceModel(defaul!!.mablagh, defaul!!.calender, defaul!!.clock))
            }
            if (defaul!!.spinner2BottomSheet == null || defaul!!.spinner2BottomSheet?.selectedItemPosition == -1 || defaul!!.qestpay.text.toString().isEmpty() || defaul!!.mablagh.text.toString().isEmpty() || defaul!!.mablagh.text.toString().isEmpty()) {
                Toast.makeText(context, "لطفا تمام فیلد ها را کامل کنید", Toast.LENGTH_SHORT).show()
                return
            }
            try {
                jsonObject.put("price", defaul!!.mablagh.text.toString())
                jsonObject.put("year", defaul!!.now?.year)
                jsonObject.put("month", defaul!!.now?.month)
                jsonObject.put("day", defaul!!.now?.day)
                jsonObject.put("hour", if (defaul!!.timePickerDialog == null) "12" else defaul!!.timePickerDialog?.timePicker?.currentHour)
                jsonObject.put("minute", if (defaul!!.timePickerDialog == null) "0" else defaul!!.timePickerDialog?.timePicker?.currentMinute)
                jsonArray.put(jsonObject)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        val sharedPreferences = activity!!.getSharedPreferences("Profile", Context.MODE_PRIVATE)
        AndroidNetworking.post(getString(R.string.url) + "addBooklet/")
                .addBodyParameter("title", bookletAdd!!.booklet?.recaiver?.text.toString())
                .addBodyParameter("type", bookletAdd!!.booklet?.spinnerBottomSheetFragment?.selectedItem)
                .addBodyParameter("number", bookletAdd!!.booklet?.numberPay?.text.toString())
                .addBodyParameter("id", sharedPreferences.getString("id", null))
                .addBodyParameter("sms", bookletAdd!!.booklet?.phone)
                .addBodyParameter("notify", bookletAdd!!.booklet?.token)
                .addBodyParameter("date", jsonArray.toString())
                .addBodyParameter("how", if (defaul!!.spinner2BottomSheet == null) "0" else defaul!!.spinner2BottomSheet?.selectedItemPosition.toString())
                .addBodyParameter("index", defaul!!.numberqest.text.toString())
                .addBodyParameter("payIndex", defaul!!.qestpay.text.toString()).build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        bookletAdd!!.setResult(Activity.RESULT_OK)
                        bookletAdd!!.finish()
                    }

                    override fun onError(anError: ANError) {
                        click = false
                    }
                })
    }
}