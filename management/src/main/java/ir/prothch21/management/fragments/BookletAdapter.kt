package ir.prothch21.management.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import ir.prothch21.management.PayActivity
import ir.prothch21.management.R
import ir.prothch21.management.models.All
import kotlinx.android.synthetic.main.fragment_aghsaat.view.*
import okhttp3.Response
import java.util.*

class BookletAdapter(private val context: Context, private val bookletModels: ArrayList<All>) : RecyclerView.Adapter<BookletAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_aghsaat, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (bookletModels.isEmpty()) {
            return
        }
        holder.itemView.findViewById<View>(R.id.cardView).setOnLongClickListener { v: View? ->
            val builder = AlertDialog.Builder(context)
            builder.setTitle("حذف آیتم")
            builder.setMessage("آیا از حدف این آیتم مطمن هستید؟؟")
            builder.setPositiveButton("بله") { dialog: DialogInterface?, which: Int ->
                AndroidNetworking.post(context.getString(R.string.url) + "delBooklet/").addBodyParameter("code", bookletModels[position].id).build().getAsString(object : StringRequestListener {
                    override fun onResponse(response: String) {
                        bookletModels.removeAt(position)
                        notifyItemRemoved(position)
                    }

                    override fun onError(anError: ANError) {}
                })
            }
            builder.setNegativeButton("خیر") { dialog: DialogInterface?, which: Int -> }
            builder.show()
            true
        }
        holder.title.text = bookletModels[position].title
        holder.type.text = bookletModels[position].id
        holder.baghi.text = bookletModels[position].reminded.number.toString()
        holder.moavaghe.text = bookletModels[position].timeless.number.toString()
        holder.pardakhti.text = bookletModels[position].pay.number.toString()
        holder.payPrice.text = bookletModels[position].pay.price.toString()
        holder.baghiPrice.text = bookletModels[position].reminded.price.toString()
        holder.moavaghePrice.text = bookletModels[position].timeless.price.toString()
        holder.day_aghsaat.text = bookletModels[position].strDate
        holder.itemView.findViewById<View>(R.id.cardView).setOnClickListener {
            AndroidNetworking.post(context.getString(R.string.url) + "getController/").addBodyParameter("code", bookletModels[position].id).build().getAsOkHttpResponseAndString(object : OkHttpResponseAndStringRequestListener {
                override fun onResponse(okHttpResponse: Response, response: String) {
                    val intent = Intent(context, PayActivity::class.java)
                    intent.putExtra("json", response)
                    context.startActivity(intent)
                }

                override fun onError(anError: ANError) {}
            })
        }
        //        holder.type.setText(bookletModels.get(position).getType());
    }
    override fun getItemCount(): Int = bookletModels.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.title
        var type: TextView = itemView.type
        var day_aghsaat: TextView = itemView.day_aghsaat
        var baghi: TextView = itemView.baghi
        var moavaghe: TextView = itemView.moavaghe
        var pardakhti: TextView = itemView.pardakhti
        var payPrice: TextView = itemView.payPrice
        var moavaghePrice: TextView = itemView.moavaghePrice
        var baghiPrice: TextView = itemView.baghiPrice

    }

}