package com.asooneh.motakhasesein_asooneh.adapter.viewHolders

import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.HorizontalScrollView
import carbon.widget.TextView
import ir.protech21.utils.Utils.Companion.isJson
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_table.view.*
import kotlinx.android.synthetic.main.object_main_table_text.view.*
import org.json.JSONObject

class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var textViewAsk: TextView = itemView.textViewAsk
    private var tableLayout: TableLayout = itemView.tableLayout
    var linearLayout: LinearLayout = itemView.linearLayout
    var scrollView: HorizontalScrollView = itemView.scrollView

    fun bind(objects: JSONObject) {

    }

    fun set(objects: JSONObject): RecyclerView.ViewHolder {
        val question = objects.getString("question")
        textViewAsk.text = question
        if (objects.isNull("question") || question.isNullOrEmpty()) {
            (textViewAsk.parent as? ViewGroup)?.removeView(textViewAsk)
        }
        itemView.description?.let { (it.parent as? ViewGroup?)?.removeView(it) }
        val js = objects.getJSONArray("answer")
        if (js.isNull(0))
            return this

        if (!js.isNull(0)) {
            if (!isJson(js.getString(0)))
                return this
            if (js.getJSONArray(0).length() > 2) {
                for (i in 0 until js.length()) {
                    val layout = TableRow(itemView.context)
                    for (j in js.getJSONArray(i).length() - 1 downTo 0) {
                        val txt = TextView(itemView.context)
                        txt.gravity = Gravity.CENTER
                        txt.setStroke(ContextCompat.getColor(itemView.context, R.color.colorblue))
                        txt.strokeWidth = 2f
                        txt.setPadding(8, 8, 8, 8)
                        txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorblue))
                        txt.text = js.getJSONArray(i).getString(j)
                        layout.addView(txt)
                    }
                    tableLayout.addView(layout)
                }
            } else {
                for (i in 0 until js.length()) {
                    val layout = LayoutInflater.from(itemView.context).inflate(R.layout.object_main_table_text, tableLayout, false)
                    for (j in js.getJSONArray(i).length() - 1 downTo 0) {
                        if (j % 2 == 0) {
                            layout.key.text = js.getJSONArray(i).getString(j)
                            layout.key.textColor = ContextCompat.getColor(itemView.context, R.color.colorblue)
                            layout.key.typeFace = Typeface.createFromAsset(itemView.context.assets, itemView.context.getString(R.string.iransans))
                        } else {
                            layout.value.text = js.getJSONArray(i).getString(j)
                        }
                    }
                    linearLayout.addView(layout)
                }
            }
        }

        tableLayout.post {
            scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
        }
        return this
    }
}