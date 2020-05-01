package com.asooneh.motakhasesein_asooneh.adapter.viewHolders

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.TextView
import ir.protech21.views.JustifiedTextView
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_lable.view.textViewAsk
import kotlinx.android.synthetic.main.object_main_lable_show.view.*
import org.json.JSONObject

class LabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textViewAsk: TextView = itemView.textViewAsk

    fun bind(objects: JSONObject) {
        textViewAsk.text = objects.getString("question")
        val js = objects.getJSONArray("answer")
        for (index in 0 until js.length()) {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.justified_text_view, itemView.answers, false) as JustifiedTextView
            view.text = if (!js.isNull(0)) js.getString(index) else ""
            view.setLineSpacing(15)
            view.textSize = 18f
            view.textColor = ContextCompat.getColor(itemView.context, R.color.colorblue)
            view.typeFace = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
            itemView.answers.addView(view)
        }

    }
}