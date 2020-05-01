package ir.prothch21.questions.viewHolders

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.GridLayout
import com.squareup.picasso.Picasso
import ir.prothch21.R
import kotlinx.android.synthetic.main.image_lin.view.*
import kotlinx.android.synthetic.main.object_main_image.view.*
import org.json.JSONArray
import org.json.JSONObject

class ImageViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

    val gridLayout: GridLayout = itemView.gridLayout

    fun bind(jsonObject: JSONObject) {
        val jsonArray = JSONArray(jsonObject.getString("answer"))
        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        for(i in 0 until jsonArray.length()){
            val json = jsonArray.getJSONObject(i)
            val imagelin = LayoutInflater.from(itemView.context).inflate(R.layout.image_lin,gridLayout,false)
            imagelin.text.text = json.getString("answer")
            Picasso.with(itemView.context).load(itemView.context.getString(R.string.url) +"media/"+json.getString("file")).resize(100,100).into(imagelin.image)
            val param = GridLayout.LayoutParams(GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f))
            param.width = 0
            gridLayout.addView(imagelin,param)
        }
    }
}