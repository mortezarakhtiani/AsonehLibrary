package ir.prothch21.questions.viewHolders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import ir.prothch21.questions.ObjectAdapter
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_text_box.view.*
import org.json.JSONArray
import org.json.JSONObject

class SwitchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private lateinit var json: JSONObject
    val linea = itemView.linea
    fun bind(jsonObject: JSONObject, objectAdapter: ObjectAdapter, rec: RecyclerView?) {
        this.json = jsonObject

        val jsonArray = JSONArray(jsonObject.getString("answer"))
        linea.removeAllViews()
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            val switch = LayoutInflater.from(itemView.context).inflate(R.layout.object_switch, linea, false) as androidx.appcompat.widget.SwitchCompat
            switch.text = json.getString("answer")
            switch.id = i
            switch.tag = 0
            switch.setOnCheckedChangeListener { buttonView, isChecked ->

            }
            linea.addView(switch)
        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 14)
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONArray().apply {

            for (i in 0 until linea.childCount) {
                val checkBox = linea.getChildAt(i) as androidx.appcompat.widget.SwitchCompat
                if (checkBox.isChecked) put(checkBox.text.toString())
            }

        })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }


}