package ir.prothch21.notes.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import ir.protech21.persian.utils.PersianCalendar
import ir.prothch21.notes.R
import ir.prothch21.notes.model.RememberModel
import ir.prothch21.notes.remember.Remember
import kotlinx.android.synthetic.main.remember_item.view.*
import kotlin.collections.ArrayList

class RememberAdapter(private val activity: Activity, private val rememberModels: ArrayList<RememberModel>) : RecyclerView.Adapter<RememberAdapter.ViewHolder>() {
    var now: PersianCalendar = PersianCalendar()
    var booleans = ArrayList<Boolean>()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.remember_item, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        setAnimation(viewHolder.itemView, i)
        viewHolder.time.text = rememberModels[i].hour + " : " + rememberModels[i].minute
        try {
            now.setPersianDate(rememberModels[i].year.toInt(), rememberModels[i].month.toInt() - 1, rememberModels[i].day.toInt())
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        booleans.add(false)
        viewHolder.date.text = String.format("%s %s %s %s", now.persianWeekDayName, now.persianDay, now.persianMonthName, now.persianYear)
        viewHolder.title.text = rememberModels[i].title
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(activity, Remember::class.java)
            intent.putExtra("update", true)
            intent.putExtra("title", rememberModels[i].title)
            intent.putExtra("date", String.format("%s %s %s %s", now.persianWeekDayName, now.persianDay, now.persianMonthName, now.persianYear))
            intent.putExtra("hour", rememberModels[i].hour)
            intent.putExtra("minute", rememberModels[i].minute)
            intent.putExtra("des", rememberModels[i].des)
            intent.putExtra("location", rememberModels[i].location)
            intent.putExtra("code", rememberModels[i].code)
            activity.startActivityForResult(intent,2585)
        }

        viewHolder.title.setOnCheckedChangeListener { buttonView, isChecked ->
            booleans[i] = isChecked
            function?.invoke(i, booleans)
        }
    }

    private var lastPosition = -1
    private fun setAnimation(itemView: View, i: Int) {
        if (i > lastPosition) {
            val animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in)
            animation.duration = 1000
            itemView.startAnimation(animation)
            lastPosition = i
        }
    }

    override fun getItemCount(): Int {
        return rememberModels.size
    }

    var function: ((Int, ArrayList<Boolean>) -> Unit)? = null
    fun setOnItemCheckedListener(function: (Int, ArrayList<Boolean>) -> Unit) {
        this.function = function
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.title
        val date = itemView.date
        val time = itemView.time


    }

}