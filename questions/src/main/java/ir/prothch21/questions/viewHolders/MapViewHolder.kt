package ir.prothch21.questions.viewHolders

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ir.protech21.filepicker.FilePicker
import ir.protech21.utils.Utils
import ir.prothch21.activities.map.MapActivity
import ir.prothch21.R
import kotlinx.android.synthetic.main.object_main_map.view.*
import kotlinx.android.synthetic.main.object_main_map.view.address
import kotlinx.android.synthetic.main.object_main_map.view.google_maps
import kotlinx.android.synthetic.main.object_main_map.view.mapView
import org.json.JSONArray
import org.json.JSONObject


class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var json: JSONObject
    val textViewAsk = itemView.textViewAsk
    val description = itemView.description
    val google_maps = itemView.google_maps
    val mapView = itemView.mapView
    var latlng: LatLng? = null

    fun bind(objects: JSONObject) {
        this.json = objects
        textViewAsk.text = objects.getString("question")
        description.text = objects.getString("description")
        mapView.setOnClickListener {
            if (!(itemView.getTag(R.id.BOOLEAN) as Boolean)) {
                itemView.setTag(R.id.BOOLEAN, true)
            }
            if (ActivityCompat.checkSelfPermission(itemView.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(itemView.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(itemView.getTag(R.id.ACTIVITY) as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 8585)
                return@setOnClickListener
            }
            getMap(itemView.getTag(R.id.ACTIVITY) as AppCompatActivity)
        }
    }

    private fun getMap(activity: AppCompatActivity) {
        val sharedPreferences = activity.getSharedPreferences("Profile", Context.MODE_PRIVATE)
        val intent = Intent(activity, MapActivity::class.java)
        intent.putExtra("Latitude", java.lang.Double.parseDouble(sharedPreferences.getString("Latitude", "35.688392")!!))
        intent.putExtra("Longitude", java.lang.Double.parseDouble(sharedPreferences.getString("Longitude", "51.389130")!!))
        FilePicker(activity).setOnResponse(intent) { data ->
            latlng = LatLng((data?.getDoubleExtra("Latitude", 0.0)!!), data.getDoubleExtra("Longitude", 0.0))
            with(mapView) {
                onCreate(null)
                this?.getMapAsync {
                    MapsInitializer.initialize(context.applicationContext)
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))
                    it.addMarker(MarkerOptions().position(latlng!!))
                    it.mapType = GoogleMap.MAP_TYPE_NORMAL
                    it.uiSettings.setAllGesturesEnabled(false)
                }
            }
        }


    }


    fun getData(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", json.getString("question_type").toInt())
        jsonObject.put("question", json.getString("question"))
        jsonObject.put("answer", JSONObject().apply {
            put("Latitude", latlng?.latitude)
            put("Longitude", latlng?.longitude)
            put("Address", itemView.address.text.toString())
        })
        jsonObject.put("other_question", JSONArray())
        return jsonObject
    }
}