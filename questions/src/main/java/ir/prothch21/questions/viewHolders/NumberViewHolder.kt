package ir.prothch21.questions.viewHolders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_number.view.*
import kotlinx.android.synthetic.main.object_text_box.view.*
import org.json.JSONArray
import org.json.JSONObject

class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val linea = itemView.linea

    fun bind(jsonObject: JSONObject) {
        this.json = jsonObject
        val jsonArray = jsonObject.getJSONArray("answer")
        for (i in 0 until jsonArray.length()) {
            val editText = LayoutInflater.from(itemView.context).inflate(R.layout.object_number, linea, false)
            editText.text.text = jsonArray.getJSONObject(i).getString("answer")
            linea.addView(editText)
        }
    }


    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", json.getString("question_type").toInt())
        jsonObject.put("question", json.getString("question"))
        val jsonArray = JSONArray()
        for (i in 0 until linea.childCount) {
            val view = linea.getChildAt(i)
            jsonArray.put(JSONObject().apply {
                put("text",view.text.text.toString())
                put("value",view.numberChoicer.text.toString())
            })
        }

        jsonObject.put("answer", jsonArray)
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }

}