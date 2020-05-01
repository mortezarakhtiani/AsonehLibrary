package ir.prothch21.show.adapter.viewHolders

import android.graphics.Typeface
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.EditText
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_text_box.view.*
import org.json.JSONObject

class TextViewViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

    val linea = itemView.linea

    fun bind(jsonObject: JSONObject) {
        if (jsonObject.getInt("type") == 4) {
            val jsonArray = jsonObject.getJSONArray("answer")
            for(i in 0 until jsonArray.length()){
                val editText = LayoutInflater.from(itemView.context).inflate(R.layout.object_textbox,linea,false) as EditText
                editText.hint = jsonArray.getString(i)
                editText.typeface = Typeface.createFromAsset(itemView.context.assets,"fonts/iransans.ttf")
//                    editText.inputType = if (json.getString("type")=="1") InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD else if (json.getString("type")=="2")InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT

                linea.addView(editText)
            }

        }
        if (jsonObject.getInt("type") == 9) {
            val editText = LayoutInflater.from(itemView.context).inflate(R.layout.object_textbox,linea,false) as EditText
            editText.typeface = Typeface.createFromAsset(itemView.context.assets,"fonts/iransans.ttf")
            editText.hint = "کد ملی را وارد کنید"
            editText.inputType = InputType.TYPE_CLASS_NUMBER

            linea.addView(editText)

        }
    }
}