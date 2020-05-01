package ir.prothch21.show.adapter.viewHolders

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import ir.huri.jcal.JalaliCalendar
import kotlinx.android.synthetic.main.object_main_map.view.*

import org.json.JSONObject

class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var now: JalaliCalendar? = null
    val textViewAsk = itemView.textViewAsk
    val google_maps = itemView.google_maps
    val mapView = itemView.mapView
    val address = itemView.address

    fun bind(objects: JSONObject) {
        textViewAsk.text = objects.getString("question")
        val json = objects.getJSONObject("answer")
        address.setText(json.getString("Address"))
        if (!json.isNull("Latitude") && !json.isNull("Longitude")) {
            val latlng = LatLng(json.getDouble("Latitude"), json.getDouble("Longitude"))

            with(mapView) {
                onCreate(null)
                this?.getMapAsync {
                    com.google.android.gms.maps.MapsInitializer.initialize(context.applicationContext)
                    it.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(latlng, 15f))
                    it.addMarker(com.google.android.gms.maps.model.MarkerOptions().position(latlng))
                    it.mapType = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
                    it.uiSettings.setAllGesturesEnabled(false)
                }
            }
        } else
            (google_maps.parent as ViewGroup).removeView(google_maps)

    }


}