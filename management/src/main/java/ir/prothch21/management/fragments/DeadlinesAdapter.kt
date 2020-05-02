package ir.prothch21.management.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import ir.protech21.persian.date.DatePickerDialog
import ir.protech21.persian.utils.PersianCalendar
import ir.prothch21.management.R
import ir.prothch21.management.models.DeadlinesModel
import kotlinx.android.synthetic.main.dead_lines_item.view.*
import okhttp3.Response
import java.util.*

class DeadlinesAdapter(private val activity: Deadlines, private var deadlinesModels: ArrayList<DeadlinesModel>) : RecyclerView.Adapter<DeadlinesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return   ViewHolder(LayoutInflater.from(activity.activity).inflate(R.layout.dead_lines_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemView.setOnClickListener {
            val dialog = Dialog(activity.activity!!)
            dialog.setContentView(R.layout.pay_dialog)
            val textView = dialog.findViewById<Button>(R.id.date)
            val now = PersianCalendar()
            textView.text = String.format("%s/%s/%s", now.persianYear, now.persianMonth, now.persianDay)
            textView.setOnClickListener {
                val datePickerDialog = DatePickerDialog
                        .newInstance({ view, year, monthOfYear, dayOfMonth -> textView.text = String.format("%s/%s/%s", year, monthOfYear + 1, dayOfMonth) }, now.persianYear, now.persianMonth, now.persianDay)
                datePickerDialog.isThemeDark = true
                datePickerDialog.show(activity.activity!!.fragmentManager, "tpd")
            }
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.findViewById<View>(R.id.sabt).setOnClickListener {
                val s = (dialog.findViewById<View>(R.id.des) as EditText).text.toString()
                AndroidNetworking.post(activity.getString(R.string.url) + "PayQ/").addBodyParameter("id", deadlinesModels[i].id).addBodyParameter("des", s).addBodyParameter("paydate", textView.text.toString()).build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                    override fun onResponse(okHttpResponse: Response, response: String) {
                        if (deadlinesModels.size == 1) {
                            deadlinesModels = ArrayList()
                            activity.recyclerView?.adapter = DeadlinesAdapter(activity, deadlinesModels)
                        } else {
                            deadlinesModels.removeAt(i)
                        }
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }

                    override fun onError(anError: ANError) {}
                })
            }
            dialog.show()
        }
        viewHolder.title.text = deadlinesModels[i].title
        viewHolder.type.text = deadlinesModels[i].type
        viewHolder.price.text = deadlinesModels[i].price
        viewHolder.date.text = deadlinesModels[i].deadtime
    }

    override fun getItemCount(): Int {
        return deadlinesModels.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.title
        var type = itemView.type
        var price = itemView.price
        var date = itemView.date

    }

}