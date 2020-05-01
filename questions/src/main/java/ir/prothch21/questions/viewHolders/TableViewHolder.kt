package ir.prothch21.questions.viewHolders

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
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_table.view.*
import kotlinx.android.synthetic.main.object_main_table_text.view.*
import org.json.JSONArray
import org.json.JSONObject

class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var textViewAsk: TextView = itemView.textViewAsk
    var description: TextView = itemView.description
    private var tableLayout: TableLayout = itemView.tableLayout
    var linearLayout: LinearLayout = itemView.linearLayout
    var scrollView: HorizontalScrollView = itemView.scrollView

    fun bind(objects: JSONObject) {
        if (objects.getString("question_type") == "1") {
            val question = objects.getString("question")
            textViewAsk.text = question
            if (question.isNullOrEmpty()) {
                (textViewAsk.parent as ViewGroup).removeView(textViewAsk)
            }
            description.text = objects.getString("description")
            itemView.setTag(R.id.BOOLEAN, true)
            val js = objects.getJSONArray("answer")
            if (js.isNull(0))
                return

            val array = JSONArray(js.getJSONObject(0).getString("text"))
            if (!array.isNull(0)) {
                if (array.getJSONArray(0).length() > 2) {
                    for (i in 0 until array.length()) {
                        val layout = TableRow(itemView.context)
                        for (j in array.getJSONArray(i).length() - 1 downTo 0) {
                            val txt = TextView(itemView.context)
                            txt.gravity = Gravity.CENTER
                            txt.setStroke(ContextCompat.getColor(itemView.context, R.color.colorblue))
                            txt.strokeWidth = 2f
                            txt.setPadding(8, 8, 8, 8)
                            txt.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorblue))
                            txt.text = array.getJSONArray(i).getString(j)
                            layout.addView(txt)
                        }
                        tableLayout.addView(layout)
                    }
                } else {
                    for (i in 0 until array.length()) {
                        val layout = LayoutInflater.from(itemView.context).inflate(R.layout.object_main_table_text, tableLayout, false)
                        for (j in array.getJSONArray(i).length() - 1 downTo 0) {
                            if (j % 2 == 0) {
                                layout.key.text = array.getJSONArray(i).getString(j)
                            } else {
                                layout.value.text = array.getJSONArray(i).getString(j)
                            }
                        }
                        linearLayout.addView(layout)
                    }
                }
            }

            tableLayout.post {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
            }

        }
    }
}