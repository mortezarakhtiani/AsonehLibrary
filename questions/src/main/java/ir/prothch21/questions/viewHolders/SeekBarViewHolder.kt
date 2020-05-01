package ir.prothch21.questions.viewHolders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.appyvet.materialrangebar.RangeBar
import ir.prothch21.R

import kotlinx.android.synthetic.main.object_seekbar.view.*
import org.json.JSONArray
import org.json.JSONObject

class SeekBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val linea = itemView.linea
    lateinit var rangeBar: RangeBar

    fun bind(jsonObject: JSONObject) {
        this.json = jsonObject

        val jsonArray = JSONArray(jsonObject.getString("answer"))
        for (i in 0 until jsonArray.length()) {
            val json = jsonArray.getJSONObject(i)
            rangeBar = LayoutInflater.from(itemView.context).inflate(R.layout.objectseekbar, linea, false) as RangeBar
            rangeBar.setRangeBarEnabled(jsonObject.getString("question_type") == "11")
            val text = json.getString("text")
            if (text.isEmpty()) {
                continue
            }
            val textJson = JSONObject(text)
            val jsonarray = textJson.getJSONArray("value")
            val list = ArrayList<CharSequence>()
            for (j in 0 until jsonarray.length()) {
                list.add(jsonarray.getString(j))
            }
            if (textJson.getString("type") == "0") {
                rangeBar.tickEnd = list[1].toString().toFloat()

                rangeBar.tickStart = list[0].toString().toFloat()
            } else {
                rangeBar.tickEnd = list.size.toFloat() - 1
                rangeBar.tickBottomLabels = list.toTypedArray()
            }
//                rangeBar.tickBottomLabels = list.toTypedArray()
            linea.addView(rangeBar)
        }

    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", json.getString("question_type").toInt())
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONObject().apply {
            put("leftPinValue", rangeBar.leftPinValue)
            put("rightPinValue", rangeBar.rightPinValue)
        })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }

}