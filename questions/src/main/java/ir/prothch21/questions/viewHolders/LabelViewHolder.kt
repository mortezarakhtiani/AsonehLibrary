package ir.prothch21.questions.viewHolders

import android.graphics.Typeface
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.TextView
import ir.protech21.views.JustifiedTextView
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_lable.view.*
import org.json.JSONObject

class LabelViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    var textViewAsk: TextView = itemView.textViewAsk
    var description: TextView = itemView.description
    var justifiedTextView: JustifiedTextView = itemView.justifiedTextView

    fun bind( objects: JSONObject) {
        itemView.setTag(R.id.BOOLEAN, true)
        textViewAsk.text = objects.getString("question")
        description.text = objects.getString("description")
        val js = objects.getJSONArray("answer")
        justifiedTextView.text = if (!js.isNull(0)){js.getJSONObject(0).getString("text")}else ""
        justifiedTextView.setLineSpacing(15)
        justifiedTextView.textColor= ContextCompat.getColor(itemView.context, R.color.colorblue)
        justifiedTextView.typeFace = Typeface.createFromAsset(itemView.context.assets, "fonts/iransans.ttf")
    }


}