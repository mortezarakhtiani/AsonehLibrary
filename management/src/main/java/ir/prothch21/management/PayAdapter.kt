package ir.prothch21.management

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.utils.PersianCalendar
import okhttp3.Response
import java.util.*

internal class PayAdapter(private val activity: Activity, private val payModels: ArrayList<PayModel>) : RecyclerView.Adapter<PayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.pay_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.price.text = payModels[i].price
        viewHolder.date.text = String.format("%s/%s/%s", payModels[i].year, payModels[i].month, payModels[i].day)
        viewHolder.position.text = i.toString()
        when (payModels[i].pay) {
            "" -> viewHolder.view.setBackgroundColor(activity.resources.getColor(R.color.colorblue))
            "true" -> {
                viewHolder.view.setBackgroundColor(Color.GREEN)
                viewHolder.payRel.visibility = View.VISIBLE
                viewHolder.paydate.text = payModels[i].paydate
                viewHolder.des.text = payModels[i].des
            }
            "false" -> viewHolder.view.setBackgroundColor(Color.RED)
        }
        viewHolder.itemView.setOnClickListener {
            if (payModels[i].pay == "true") {
                Toast.makeText(activity, "این قسط پرداخت شده است", Toast.LENGTH_SHORT).show()
            } else {
                val dialog = Dialog(activity)
                dialog.setContentView(R.layout.pay_dialog)
                val textView = dialog.findViewById<Button>(R.id.date)
                val now = PersianCalendar()
                textView.text = String.format("%s/%s/%s", now.persianYear, now.persianMonth, now.persianDay)
                textView.setOnClickListener {
                    val datePickerDialog = DatePickerDialog
                            .newInstance({ view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int -> textView.text = String.format("%s/%s/%s", year, monthOfYear + 1, dayOfMonth) }, now.persianYear, now.persianMonth, now.persianDay)
                    datePickerDialog.isThemeDark = true
                    datePickerDialog.show(activity.fragmentManager, "tpd")
                }
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.findViewById<View>(R.id.sabt).setOnClickListener {
                    val s = (dialog.findViewById<View>(R.id.des) as EditText).text.toString()
                    AndroidNetworking.post(activity.getString(R.string.url) + "PayQ/").addBodyParameter("id", payModels[i].id).addBodyParameter("des", s).addBodyParameter("paydate", textView.text.toString()).build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                        override fun onResponse(okHttpResponse: Response, response: String) {
                            payModels[i] = PayModel(payModels[i].id, payModels[i].year, payModels[i].month, payModels[i].day, payModels[i].hour, payModels[i].minute, "true", payModels[i].price, textView.text.toString(), s)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        }

                        override fun onError(anError: ANError) {}
                    })
                }
                dialog.show()
            }
        }
        viewHolder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return payModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var price: TextView
        var date: TextView
        var paydate: TextView
        var position: TextView
        var des: TextView
        var payRel: LinearLayout
        var view: View

        init {
            price = itemView.findViewById(R.id.price)
            date = itemView.findViewById(R.id.date)
            paydate = itemView.findViewById(R.id.paydate)
            position = itemView.findViewById(R.id.position)
            payRel = itemView.findViewById(R.id.payRel)
            view = itemView.findViewById(R.id.view)
            des = itemView.findViewById(R.id.des)
        }
    }

}