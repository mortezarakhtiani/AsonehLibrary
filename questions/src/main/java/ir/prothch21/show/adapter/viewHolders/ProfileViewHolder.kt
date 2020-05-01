package ir.prothch21.show.adapter.viewHolders

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.prothch21.R
import ir.prothch21.activities.gallery.Gallery
import ir.prothch21.activities.gallery.GalleryModel
import kotlinx.android.synthetic.main.object_profile.view.*
import org.json.JSONObject
import java.util.*

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title = itemView.title
    val image = itemView.image

    fun bind(jsonObject: JSONObject) {
        val json = jsonObject.getJSONObject("answer")
        val galleryModels: ArrayList<GalleryModel> = ArrayList()
//        val bitmap = AppCompatResources.getDrawable(itemView.context,R.drawable.logo)?.toBitmap(64,64,Bitmap.Config.ARGB_8888)
        Picasso.get().load(itemView.context.getString(R.string.image_url) + json.getString("icon")).placeholder(R.drawable.ic_logo).resize(64, 64).into(image)
        galleryModels.add(GalleryModel(itemView.context.getString(R.string.image_url) + json.getString("icon"), "title"))
        title.text = json.getString("title")
        image.setOnClickListener {
            val intent = Intent(it.context, Gallery::class.java)
            intent.putExtra("gallery", galleryModels)
            it.context.startActivity(intent)
        }

    }
}