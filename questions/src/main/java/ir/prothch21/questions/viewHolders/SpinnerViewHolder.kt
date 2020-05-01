package ir.prothch21.questions.viewHolders

import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.prothch21.questions.ObjectAdapter
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_image.view.*
import kotlinx.android.synthetic.main.object_main_radio.view.*
import kotlinx.android.synthetic.main.object_main_spinner.view.*
import kotlinx.android.synthetic.main.object_main_spinner.view.description
import kotlinx.android.synthetic.main.object_main_spinner.view.recyclerView
import kotlinx.android.synthetic.main.object_main_spinner.view.textViewAsk
import org.json.JSONArray
import org.json.JSONObject

@Suppress("UNREACHABLE_CODE")
class SpinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val searchableSpinner = itemView.searchableSpinner
    var string: String? = null

    fun bind(jsonObject: JSONObject, objectAdapter: ObjectAdapter) {
        this.json = jsonObject
        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        val jsonArray = JSONArray(jsonObject.getString("answer"))
        searchableSpinner.setTitle(jsonObject.getString("question"))
        searchableSpinner.setPositiveButton("لغو")
        val array = ArrayList<String>()
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            array.add(json.getString("answer"))
        }
        searchableSpinner.adapter = ArrayAdapter<String>(itemView.context, R.layout.simple_spinner_item, array)
        if (array.isNotEmpty())
            string = jsonArray.getJSONObject(0).getString("answer")
        searchableSpinner.setOnItemClick {
            string = jsonArray.getJSONObject(it).getString("answer")
            objectAdapter.addQuestions(itemView.recyclerView, jsonArray.getJSONObject(it).getString("other_question_id"))
        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 7)
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONArray().apply { put(string) })
        val adapter = itemView.recyclerView.adapter as? ObjectAdapter
        jsonObject.put("other_question", adapter?.getData() ?: JSONArray())
        return jsonObject
    }
}