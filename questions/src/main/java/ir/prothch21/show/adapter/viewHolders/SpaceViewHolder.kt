package com.asooneh.motakhasesein_asooneh.adapter.viewHolders

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.protech21.utils.Utils.Companion.Print
import kotlinx.android.synthetic.main.object_main_lable.view.textViewAsk
import kotlinx.android.synthetic.main.object_main_lable_show.view.*
import kotlinx.android.synthetic.main.object_spance.view.*
import org.json.JSONObject

class SpaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var content = itemView.content

    fun bind(objects: JSONObject) {
        val js = objects.getJSONObject("answer")
        val view = View(itemView.context)
        Print(js)
        if (!js.isNull("color"))
            view.setBackgroundColor(Color.parseColor(js.getString("color")))
        if (!js.isNull("height"))
            content.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, js.getInt("height"))

    }
}