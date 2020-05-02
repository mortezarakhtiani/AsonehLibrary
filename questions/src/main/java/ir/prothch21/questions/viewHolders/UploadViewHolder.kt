package ir.prothch21.questions.viewHolders

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import carbon.widget.GridLayout
import ir.protech21.filepicker.FilePicker
import ir.protech21.utils.Utils
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_image.view.*
import kotlinx.android.synthetic.main.object_main_image.view.description
import kotlinx.android.synthetic.main.object_main_image.view.textViewAsk
import kotlinx.android.synthetic.main.upload_lin.view.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.roundToInt

class UploadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val gridLayout = itemView.gridLayout

    fun bind(jsonObject: JSONObject, activity: AppCompatActivity) {
        this.json=jsonObject
        itemView.textViewAsk.text = jsonObject.getString("question")
        itemView.description.text = jsonObject.getString("description")
        val jsonArray = JSONArray(jsonObject.getString("answer"))
        for (i in 0 until jsonArray.length()) {
            val imagelin = LayoutInflater.from(itemView.context).inflate(R.layout.upload_lin, gridLayout, false)
            imagelin.image.setImageResource(R.drawable.upload)
            imagelin.image.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 8585)
                    return@setOnClickListener
                }
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"

                FilePicker(activity).setOnResponse(Intent.createChooser(intent, "Select Picture")){it->
                    imagelin.image.setImageURI(it?.data)
                    imagelin.tag = Utils.getPath(activity, it?.data)
                    if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                        itemView.setTag(R.id.BOOLEAN, true)
                    }
                }
            }
            val param = GridLayout.LayoutParams(GridLayout.spec(
                    GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f))
            param.width = 0
            param.height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    100F,
                    activity.resources.displayMetrics
            ).roundToInt()
            gridLayout.addView(imagelin, param)
        }
    }

    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", 6)
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONArray().apply { for(i in 0 until gridLayout.childCount){ put(gridLayout.getChildAt(i).tag as? String) } })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }
}