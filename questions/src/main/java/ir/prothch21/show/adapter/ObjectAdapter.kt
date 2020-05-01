package ir.prothch21.show.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asooneh.motakhasesein_asooneh.adapter.viewHolders.*
import ir.prothch21.R
import ir.prothch21.show.adapter.viewHolders.ImageViewHolder
import ir.prothch21.show.adapter.viewHolders.MapViewHolder
import ir.prothch21.show.adapter.viewHolders.ProfileViewHolder
import org.json.JSONObject
import java.util.*


class ObjectAdapter(objects: List<JSONObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var objects: ArrayList<JSONObject> = ArrayList(objects)

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder = when (objects[position].getInt("type")) {
        -1 -> SpaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_spance, parent, false))
        0 -> LabelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_main_lable_show, parent, false))
        1 -> TableViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_main_table, parent, false)).set(objects[position])
        6 -> ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_main_image, parent, false))
        16 -> MapViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_main_map, parent, false))
        20 -> ProfileViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_profile, parent, false))
        else -> LabelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.object_main_lable_show, parent, false))
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (objects[holder.adapterPosition].getInt("type")) {
            -1 -> (holder as SpaceViewHolder).bind(objects[holder.adapterPosition])
            0 -> (holder as LabelViewHolder).bind(objects[holder.adapterPosition])
            1 -> (holder as TableViewHolder).bind(objects[holder.adapterPosition])
            6 -> (holder as ImageViewHolder).bind(objects[holder.adapterPosition])
            16 -> (holder as MapViewHolder).bind(objects[holder.adapterPosition])
            20 -> (holder as ProfileViewHolder).bind(objects[holder.adapterPosition])
            else -> (holder as LabelViewHolder).bind(objects[holder.adapterPosition])
        }

    }


    override fun getItemCount(): Int = objects.size

    fun getItemView(recyclerView: RecyclerView?, i: Int, handler: Handler, function: (View) -> Unit) {
        handler.post {
            val find = recyclerView?.findViewHolderForLayoutPosition(i)
            if (find != null) {
                function(find.itemView)
            } else { //
                getItemView(recyclerView, i, handler, function)
            }
        }
    }


}
