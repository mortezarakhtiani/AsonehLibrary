package ir.prothch21.questions.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.object_main_barcode.view.*
import org.json.JSONArray
import org.json.JSONObject

class BarCodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val edit = itemView.edit

    fun bind(jsonObject: JSONObject) {
        this.json = jsonObject

        edit.setOnClickListener {
//            val details = itemView.context as Details
//            details.edit = edit
//
//            if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
//                itemView.setTag(R.id.BOOLEAN, true)
//            }
        }
        edit.setOnLongClickListener {
            edit.requestFocus()
        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", json.getString("question_type").toInt())
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONObject().apply { put("code", edit.text.toString()) })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }

}