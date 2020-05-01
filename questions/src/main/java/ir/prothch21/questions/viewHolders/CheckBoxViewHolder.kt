package ir.prothch21.questions.viewHolders

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.CheckBox
import carbon.widget.GridLayout
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_check.view.*
import kotlinx.android.synthetic.main.object_main_check.view.description
import kotlinx.android.synthetic.main.object_main_check.view.textViewAsk
import kotlinx.android.synthetic.main.object_main_radio.view.*
import org.json.JSONArray
import org.json.JSONObject

class CheckBoxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val linea = itemView.linea
    val gridLa = itemView.gridLa

    fun bind(jsonObject: JSONObject) {
        this.json = jsonObject
        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        if (jsonObject.getInt("row") < 2) {
            val jsonArray = JSONArray(jsonObject.getString("answer"))
            for (i in 0 until jsonArray.length()) {
                val checkBox = LayoutInflater.from(itemView.context).inflate(R.layout.object_checkbox, linea, false) as CheckBox
                checkBox.typeface = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
                checkBox.text = jsonArray.getJSONObject(i).getString("answer")
                if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        for (v in 0 until linea.childCount) {
                            val check = linea.getChildAt(v) as CheckBox
                            if (check.isChecked) {
                                itemView.setTag(R.id.BOOLEAN, true)
                            }
                        }
                        itemView.setTag(R.id.BOOLEAN, isChecked)
                    }
                }
                itemView.linea.addView(checkBox)
            }

        } else {
            val jsonArray = JSONArray(jsonObject.getString("answer"))
            gridLa.columnCount = jsonObject.getString("column").toInt()
            for (i in 0 until jsonArray.length()) {
                val checkBox = LayoutInflater.from(itemView.context).inflate(R.layout.object_check_box, gridLa, false) as CheckBox
                checkBox.text = jsonArray.getString(i)
                checkBox.typeface = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
                if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        for (v in 0 until linea.childCount) {
                            val check = linea.getChildAt(v) as CheckBox
                            if (check.isChecked) {
                                itemView.setTag(R.id.BOOLEAN, true)
                            }
                        }
                        itemView.setTag(R.id.BOOLEAN, isChecked)
                    }
                }
                checkBox.setButtonDrawable(null)
                gridLa.addView(checkBox)
                (checkBox.layoutParams as GridLayout.LayoutParams).columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                (checkBox.layoutParams as GridLayout.LayoutParams).height = GridLayout.LayoutParams.MATCH_PARENT
            }

        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 5)
        jsonObject.put("question", json.getString("question"))
        if (json.getInt("row") < 2) {
            jsonObject.put("answer", JSONArray().apply {

                if (json.getInt("row") < 2) {
                    for (i in 0 until linea.childCount) {
                        val checkBox = linea.getChildAt(i) as CheckBox
                        if (checkBox.isChecked) put(checkBox.text.toString())
                    }
                } else {
                    for (i in 0 until gridLa.childCount) {
                        val checkBox = gridLa.getChildAt(i) as CheckBox
                        if (checkBox.isChecked) put(checkBox.text.toString())
                    }
                }

            })
            jsonObject.put("other_question", JSONArray())
            return jsonObject
        }
        return jsonObject
    }

}