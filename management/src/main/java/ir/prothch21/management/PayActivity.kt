package ir.prothch21.management

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONException
import java.util.*

class PayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        val payRecyclerView = findViewById<RecyclerView>(R.id.payRecyclerView)
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
}