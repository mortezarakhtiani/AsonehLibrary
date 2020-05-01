package ir.prothch21.questions.viewHolders

import android.graphics.Typeface
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.EditText
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_text_box.view.*
import kotlinx.android.synthetic.main.object_text_box.view.description
import kotlinx.android.synthetic.main.object_text_box.view.textViewAsk
import org.json.JSONArray
import org.json.JSONObject

class TextViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val linea = itemView.linea

    fun bind(jsonObject: JSONObject) {
        this.json = jsonObject

        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        if (jsonObject.getString("question_type") == "4") {
            val jsonArray = jsonObject.getJSONArray("answer")
            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)
                val editText = LayoutInflater.from(itemView.context).inflate(R.layout.object_textbox, linea, false) as EditText
                editText.hint = json.getString("answer")
                editText.typeface = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
//                    editText.inputType = if (json.getString("type")=="1") InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD else if (json.getString("type")=="2")InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
                if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                    editText.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            itemView.setTag(R.id.BOOLEAN, s.toString().isNotEmpty())
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                }
                linea.addView(editText)
            }

        }
        if (jsonObject.getString("question_type") == "9") {
            val editText = LayoutInflater.from(itemView.context).inflate(R.layout.object_textbox, linea, false) as EditText
            editText.typeface = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
            editText.hint = "کد ملی را وارد کنید"
            editText.inputType = InputType.TYPE_CLASS_NUMBER
            if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        itemView.setTag(R.id.BOOLEAN, s.toString().isNotEmpty())
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
            linea.addView(editText)

        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 4)
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONArray().apply {
            for (i in 0 until linea.childCount) {
                put((linea.getChildAt(i) as? EditText)?.text.toString())
            }
        })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }
}