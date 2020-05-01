package ir.prothch21.questions.viewHolders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.RadioButton
import carbon.widget.RadioGroup
import ir.protech21.utils.Utils.Companion.Print
import ir.prothch21.questions.ObjectAdapter
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_image.view.*
import kotlinx.android.synthetic.main.object_main_radio.view.*
import kotlinx.android.synthetic.main.object_main_radio.view.description
import kotlinx.android.synthetic.main.object_main_radio.view.textViewAsk
import org.json.JSONArray
import org.json.JSONObject

class RadioButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val radioGroup = itemView.radioGroup
    val radioGrouph = itemView.radioGrouph
    lateinit var json: JSONObject
    var radioButton:RadioButton?=null

    fun bind(jsonObject: JSONObject, objectAdapter: ObjectAdapter) {
        this.json=jsonObject
        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        if (jsonObject.getString("question_type") == "3" && jsonObject.getInt("row") <= 1) {
            val jsonArray = jsonObject.getJSONArray("answer")
            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                val radiobutton = LayoutInflater.from(itemView.context).inflate(R.layout.object_radiobutton, radioGroup, false) as RadioButton
                radiobutton.text = json.getString("answer")
                radiobutton.tag = json.getString("other_question_id")
                radioGroup.addView(radiobutton)
                radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    for (childcount in 0 until group.childCount) {
                        val radio = group.getChildAt(childcount) as RadioButton
                        if (radio.isChecked) {
                            this.radioButton=radio
                            objectAdapter.addQuestions(itemView.recyclerView, radio.tag as String)
                            return@setOnCheckedChangeListener
                        }
                    }
                }
            }

        }
        if (jsonObject.getString("question_type") == "3" && jsonObject.getInt("row") > 1) {
            val jsonArray = jsonObject.getJSONArray("answer")
            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                val radiobutton = LayoutInflater.from(itemView.context).inflate(R.layout.object_main_radio_button, radioGrouph, false) as RadioButton
                radiobutton.setButtonDrawable(null)
                radiobutton.text = json.getString("answer")
                radiobutton.tag = json.getString("other_question_id")
                radioGrouph.addView(radiobutton)
                radioGrouph.setOnCheckedChangeListener { group, checkedId ->
                    for (childcount in 0 until group.childCount) {
                        val radio = group.getChildAt(childcount) as RadioButton
                        if (radio.isChecked) {
                            this.radioButton=radio
                            objectAdapter.addQuestions(itemView.recyclerView, radio.tag as String)
                            return@setOnCheckedChangeListener
                        }
                    }
                }
                val layout = RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.MATCH_PARENT, 1f)
                radiobutton.layoutParams = layout
            }

        }

    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 3)
        jsonObject.put("question",json.getString("question"))
        jsonObject.put("answer",JSONArray().apply { put(radioButton?.text) })
        val adapter = itemView.recyclerView.adapter as? ObjectAdapter
        jsonObject.put("other_question",adapter?.getData() ?: JSONArray())
        return jsonObject
    }


}