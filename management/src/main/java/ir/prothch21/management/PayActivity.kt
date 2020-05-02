package ir.prothch21.management

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import ir.prothch21.management.models.Management
import kotlinx.android.synthetic.main.activity_pay.*
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        try {
            val payModels = ArrayList<PayModel>()
            val array = JSONArray(intent.getStringExtra("json"))
            for (i in 0 until array.length()) {
                val jsonObject = array.getJSONObject(i)

                payModels.add(PayModel(jsonObject.getString("id")
                        , jsonObject.getString("year")
                        , jsonObject.getString("month")
                        , jsonObject.getString("day")
                        , jsonObject.getString("hour")
                        , jsonObject.getString("minute")
                        , jsonObject.getString("pay")
                        , jsonObject.getString("price")
                        , jsonObject.getString("paydate")
                        , jsonObject.getString("des")))
            }
            payRecyclerView.adapter = PayAdapter(this, payModels)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    class PayModel(val id: String, val year: String, val month: String, val day: String, val hour: String,val  minute: String,val  pay: String,val  price: String,val  paydate: String,val  des: String)
}