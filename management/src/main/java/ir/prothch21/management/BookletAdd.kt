package ir.prothch21.management

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.google.android.material.snackbar.Snackbar
import ir.protech21.utils.Utils.Companion.Print
import ir.prothch21.management.fragments.booklet.Booklet
import ir.prothch21.management.fragments.booklet.Booklet2
import ir.prothch21.management.fragments.booklet.Custom
import ir.prothch21.management.fragments.booklet.Default
import kotlinx.android.synthetic.main.custom_booklet2.*
import kotlinx.android.synthetic.main.fragment_booklet2.*
import kotlinx.android.synthetic.main.fragment_booklet.*
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class BookletAdd : AppCompatActivity() {
    var fragmentTransaction: FragmentTransaction? = null
    var fragmentManager: FragmentManager? = null
    var linearLayout: LinearLayout? = null
    var scrollView: HorizontalScrollView? = null
    var fragments = arrayListOf(Booklet(), Booklet2())
    var textViews = ArrayList<TextView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_booklet)
        findViewByIds()
        fragmentManager = supportFragmentManager
        addFragment()
    }

    private fun findViewByIds() {
        linearLayout = findViewById(R.id.linearLayout)
    }

    fun addFragment() {
        val count = fragmentManager!!.fragments.size
        fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction!!.replace(R.id.detailsFrameLayout, fragments[count], fragments[count]::class.java.simpleName)
        if (count > 0)
            fragmentTransaction?.addToBackStack(fragments[count]::class.java.simpleName)

        fragmentTransaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
        fragmentTransaction!!.commit()
    }


    fun save(view: View, custom: Custom?, defaul: Default?) {
        view.isEnabled = false
        val data = if (radioRealButtonGroup.position == 0) {
            JSONArray().apply {
                for (model in custom?.models!!) {
                    val jsonObject = JSONObject()
                    jsonObject.put("price", model.price)
                    jsonObject.put("year", model.year)
                    jsonObject.put("month", model.month)
                    jsonObject.put("day", model.day)
                    jsonObject.put("hour", model.hour)
                    jsonObject.put("minute", model.minute)
                    put(jsonObject)
                }
            }.toString()

        } else {
            JSONObject().apply {
                put("repeat_type", defaul?.spinner2BottomSheet?.selectedItemPosition.toString())
                put("price", defaul?.mablagh?.text.toString())
                put("numberqest", defaul?.numberqest?.text.toString())
                put("year", defaul?.now?.year)
                put("month", defaul?.now?.month)
                put("day", defaul?.now?.day)
                put("hour", defaul?.timePickerDialog?.timePicker?.currentHour)
                put("minute", defaul?.timePickerDialog?.timePicker?.currentMinute)
                put("qestpay", defaul?.qestpay?.text?.toString())
            }.toString()
        }
        val sharedPreferences = getSharedPreferences("Profile", Context.MODE_PRIVATE)

        AndroidNetworking.post(getString(R.string.url) + "addBooklet/")
                .addBodyParameter("title", intent.getStringExtra("spinner"))
                .addBodyParameter("recaiver", intent.getStringExtra("recaiver"))
                .addBodyParameter("numberPay", intent.getStringExtra("numberPay"))
                .addBodyParameter("id", sharedPreferences.getString("id", null))
                .addBodyParameter("sms", intent.getStringExtra("sms"))
                .addBodyParameter("notify", intent.getStringExtra("notify"))
                .addBodyParameter("booklet", data)
                .build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        setResult(Activity.RESULT_OK)
                        finish()
                        view.isEnabled = true
                    }

                    override fun onError(anError: ANError) {
                        if (anError.errorCode == 500)
                            Snackbar.make(window?.decorView!!, anError.errorBody, Snackbar.LENGTH_LONG).show()
                        view.isEnabled = true
                    }
                })
    }
}