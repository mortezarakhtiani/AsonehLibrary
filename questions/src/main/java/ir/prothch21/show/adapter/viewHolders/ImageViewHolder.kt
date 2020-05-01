package ir.prothch21.show.adapter.viewHolders

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.GridLayout
import com.squareup.picasso.Picasso
import ir.prothch21.R
import ir.prothch21.activities.gallery.Gallery
import ir.prothch21.activities.gallery.GalleryModel
import kotlinx.android.synthetic.main.image_lin.view.*
import kotlinx.android.synthetic.main.object_main_image.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val gridLayout: GridLayout = itemView.gridLayout
    val textViewAsk = itemView.textViewAsk

    fun bind(jsonObject: JSONObject) {
        textViewAsk.text = jsonObject.getString("question")
        val jsonArray = JSONArray(jsonObject.getString("answer"))
        val galleryModels: ArrayList<GalleryModel> = ArrayList()

        for (i in 0 until jsonArray.length()) {
            if (jsonArray.isNull(i))
                continue
            val imagelin = LayoutInflater.from(itemView.context).inflate(R.layout.image_lin, gridLayout, false)
            Picasso.with(itemView.context).load(itemView.context.getString(R.string.image_url) + jsonArray.getString(i)).resize(64, 64).into(imagelin.image)
            galleryModels.add(GalleryModel(itemView.context.getString(R.string.image_url) + jsonArray.getString(i), "title"))

            val param = GridLayout.LayoutParams(GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f))
            imagelin.setOnClickListener {
                val intent = Intent(it.context, Gallery::class.java)
                intent.putExtra("gallery", galleryModels)
                it.context.startActivity(intent)
            }
            param.width = 0
            gridLayout.addView(imagelin, param)
        }
    }
}