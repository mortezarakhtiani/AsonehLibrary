package ir.prothch21.management.fragments.booklet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.ImageView
import ir.huri.jcal.JalaliCalendar
import ir.prothch21.management.BookletAdd
import ir.prothch21.management.R
import ir.prothch21.management.bottomsheet.BottomSheetFragmentAdd
import kotlinx.android.synthetic.main.custom_booklet.*
import kotlinx.android.synthetic.main.custom_booklet_item.view.*
import java.util.*

class Custom : Fragment() {
    var jalaliCalendar: JalaliCalendar? = null
    var models: ArrayList<Model>? = null
    var adapter: Adapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.custom_booklet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bookletAdd = activity as BookletAdd?
        models = ArrayList()
        jalaliCalendar = JalaliCalendar()
        adapter = Adapter()
        recyclerView.adapter = adapter

    }


    data class Model(var day: Int, var month: Int, var year: Int, var hour: Int, var minute: Int, var price: Int)

    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_booklet_item, viewGroup, false))
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            jalaliCalendar!![models!![viewHolder.adapterPosition].year, models!![viewHolder.adapterPosition].month] = models!![viewHolder.adapterPosition].day
            viewHolder.year.text = jalaliCalendar!!.year.toString()
            viewHolder.day.text = jalaliCalendar!!.day.toString()
            viewHolder.month.text = jalaliCalendar!!.monthString
            viewHolder.time.text = "${models!![viewHolder.adapterPosition].hour} : ${models!![viewHolder.adapterPosition].minute}"
            viewHolder.price.text = models!![viewHolder.adapterPosition].price.toString() + " تومان "
            viewHolder.icon.setOnClickListener {
                models!!.removeAt(viewHolder.adapterPosition)
                notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        override fun getItemCount(): Int = models!!.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var price: TextView = itemView.price
            var time: TextView = itemView.time
            var day: TextView = itemView.day
            var month: TextView = itemView.month
            var year: TextView = itemView.year
            var icon: ImageView = itemView.icon
        }
    }
}