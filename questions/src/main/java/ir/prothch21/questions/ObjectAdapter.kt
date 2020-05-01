package ir.prothch21.questions

import android.app.Activity
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import ir.prothch21.questions.viewHolders.*
import ir.protech21.utils.Utils.Companion.Print
import ir.prothch21.R
import org.json.JSONArray
import org.json.JSONObject
import kotlin.collections.ArrayList


class ObjectAdapter(objects: List<JSONObject>, val activity: Activity, val recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var objects: ArrayList<JSONObject>
    var array: ArrayList<View>
        get() {
            val array = ArrayList<View>()
            for (i in objects.indices) {
                val holder = recyclerView.findViewHolderForAdapterPosition(i)
                array.add(holder?.itemView as View)
            }
            return array
        }

    val checked: Boolean
        get() {
            for (i in objects.indices) {
                val holder = recyclerView.findViewHolderForAdapterPosition(i)
                if (holder != null && !(holder.itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                    return false
                }
            }
            return true
        }


    init {
        this.array = ArrayList()
        this.objects = ArrayList(objects)
    }

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): RecyclerView.ViewHolder = when (itemViewType) {
        0 -> LabelViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_lable, parent, false))
        1 -> TableViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_table, parent, false))
        2 -> ImageViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_image, parent, false))
        3 -> RadioButtonViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_radio, parent, false))
        4 -> TextViewViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_text_box, parent, false))
        5 -> CheckBoxViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_check, parent, false))
        6 -> UploadViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_image, parent, false))
        7 -> SpinnerViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_spinner, parent, false))
        8 -> DateViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_date, parent, false))
        9 -> TextViewViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_text_box, parent, false))
        10, 11 -> SeekBarViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_seekbar, parent, false))
        12 -> BarCodeViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_barcode, parent, false))
        13 -> DateViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_date, parent, false))
        14 -> SwitchViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_check, parent, false))
        15 -> NumberViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_check, parent, false))
        16 -> MapViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_map, parent, false))
        17 -> ButtonViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_linear, parent, false))
        else -> LabelViewHolder(LayoutInflater.from(activity).inflate(R.layout.object_main_lable, parent, false))
    }


    override fun getItemViewType(position: Int): Int = objects[position].getInt("question_type")

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setTag(R.id.BOOLEAN, objects[position].getString("must") != "true")
        holder.itemView.setTag(R.id.ACTIVITY, activity)
        when (holder.itemViewType) {
            0 -> (holder as LabelViewHolder).bind(objects[position])
            1 -> (holder as TableViewHolder).bind(objects[position])
            2 -> (holder as ImageViewHolder).bind(objects[position])
            3 -> (holder as RadioButtonViewHolder).bind(objects[position], this)
            4, 9 -> (holder as TextViewViewHolder).bind(objects[position])
            5 -> (holder as CheckBoxViewHolder).bind(objects[position])
            6 -> (holder as UploadViewHolder).bind(objects[position], activity as AppCompatActivity)
            7 -> (holder as SpinnerViewHolder).bind(objects[position], this)
            8, 13 -> (holder as DateViewHolder).bind(objects[position])
            10, 11 -> (holder as SeekBarViewHolder).bind(objects[position])
            12 -> (holder as BarCodeViewHolder).bind(objects[position])
            14 -> (holder as SwitchViewHolder).bind(objects[position], this, recyclerView)
            15 -> (holder as NumberViewHolder).bind(objects[position])
            16 -> (holder as MapViewHolder).bind(objects[position])
            17 -> (holder as ButtonViewHolder).bind(activity as AppCompatActivity, objects[position])
        }

        holder.itemView.setTag(R.id.QUESTION, holder.itemView)


    }


    fun getData(): JSONArray {
        val jsonArray = JSONArray()
        for (i in 0 until itemCount) {
            val json = when (val find = recyclerView.findViewHolderForLayoutPosition(i)) {
                is RadioButtonViewHolder -> {
                    find.getData()
                }
                is TextViewViewHolder -> {
                    find.getData()
                }
                is CheckBoxViewHolder -> {
                    find.getData()
                }
                is UploadViewHolder -> {
                    find.getData()
                }
                is SpinnerViewHolder -> {
                    find.getData()
                }
                is DateViewHolder -> {
                    find.getData()
                }
                is SeekBarViewHolder -> {
                    find.getData()
                }
                is BarCodeViewHolder -> {
                    find.getData()
                }
                is SwitchViewHolder -> {
                    find.getData()
                }
                is NumberViewHolder -> {
                    find.getData()
                }
                is MapViewHolder -> {
                    find.getData()
                }
                else -> {
                    JSONObject()
                }
            }
            if (json.length() > 0)
                jsonArray.put(json)

        }
        return jsonArray
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

    fun addQuestions(recycler: carbon.widget.RecyclerView, id: String) {
        if (id == "null") {
            recycler.adapter = ObjectAdapter(ArrayList(), activity, recycler)
            return
        }
        AndroidNetworking.post(recycler.context?.getString(R.string.url) + "details/").addBodyParameter("id", id)
                .build().getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        val arrayList = ArrayList<JSONObject>()
                        for (i in 0 until response.length()) {
                            arrayList.add(response.getJSONObject(i))
                        }
                        Print(response.toString())
                        val adapter = ObjectAdapter(arrayList, activity, recycler)
                        recycler.adapter = adapter
                        adapter.notifyItemRangeInserted(0, response.length())
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
    }


}
