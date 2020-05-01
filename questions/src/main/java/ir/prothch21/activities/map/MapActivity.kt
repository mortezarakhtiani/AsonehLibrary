package ir.prothch21.activities.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.graphics.Typeface
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import ir.prothch21.R
import kotlinx.android.synthetic.main.activity_map.*
import java.util.*

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    var map: GoogleMap? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var placesClient: PlacesClient? = null
    var predictions: List<AutocompletePrediction>? = null
    var location: Location? = null
    var locationCallback: LocationCallback? = null
    var mapView: View? = null
    var Zoom = 17f
    var token: AutocompleteSessionToken? = null
    var point: Point? = null
    var locale: Locale? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locale = Locale("fa")
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, baseContext.resources.displayMetrics)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mapView = mapFragment.view
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        Places.initialize(this, "AIzaSyA_qLPvKBnRJcdaoQrl4l6GqmZ6K0MxzRk")
        placesClient = Places.createClient(this)
        token = AutocompleteSessionToken.newInstance()
        materialSearchBarEvent()
    }

    private fun materialSearchBarEvent() {
        searchBar!!.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
            override fun onSearchStateChanged(enabled: Boolean) {}
            override fun onSearchConfirmed(text: CharSequence) {
                startSearch(text.toString(), true, null, true)
            }

            override fun onButtonClicked(buttonCode: Int) {
                when (buttonCode) {
                    MaterialSearchBar.BUTTON_NAVIGATION -> searchBar!!.disableSearch()
                    MaterialSearchBar.BUTTON_BACK -> {
                    }
                }
            }
        })
        val textView = searchBar!!.findViewById<TextView>(R.id.mt_placeholder)
        textView.typeface = Typeface.createFromAsset(assets, getString(R.string.iransans))
        searchBar!!.addTextChangeListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val predictionsRequest = FindAutocompletePredictionsRequest
                        .builder().setCountry("ir").setTypeFilter(TypeFilter.ADDRESS).setSessionToken(token).setQuery(s.toString()).build()
                placesClient!!.findAutocompletePredictions(predictionsRequest).addOnCompleteListener { task: Task<FindAutocompletePredictionsResponse?> ->
                    if (task.isSuccessful) {
                        val predictionsResponse = task.result
                        if (predictionsResponse != null) {
                            predictions = predictionsResponse.autocompletePredictions
                            val strings: MutableList<String?> = ArrayList()
                            for (prediction in predictions!!) {
                                strings.add(prediction.getFullText(null).toString())
                            }
                            searchBar!!.updateLastSuggestions(strings)
                            if (!searchBar!!.isSuggestionsVisible) {
                                searchBar!!.showSuggestionsList()
                            }
                        }
                    } else {
                        Log.e("Asooneh", "onComplete: ", task.exception)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        searchBar!!.setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener {
            override fun OnItemClickListener(position: Int, v: View) {
                if (position >= predictions!!.size) {
                    return
                }
                searchBar!!.text = searchBar!!.lastSuggestions[position].toString()
                Handler().postDelayed({ searchBar!!.clearSuggestions() }, 1000)
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm?.hideSoftInputFromWindow(searchBar!!.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
                val placeFields = Arrays.asList(Place.Field.LAT_LNG)
                val fetchPlaceRequest = FetchPlaceRequest.builder(predictions!![position].placeId, placeFields).build()
                placesClient!!.fetchPlace(fetchPlaceRequest).addOnSuccessListener { fetchPlaceResponse ->
                    val place = fetchPlaceResponse.place
                    val latLng = place.latLng
                    if (latLng != null) {
                        map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, Zoom))
                    }
                }.addOnFailureListener { }
            }

            override fun OnItemDeleteListener(position: Int, v: View) {}
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map))
        googleMap.setOnCameraMoveListener {
            //                point = new Point(mapView.getWidth()/2, mapView.getHeight()/2);
//                lastLatLng = googleMap.getProjection().fromScreenLocation(point);
        }
        locationButton!!.setOnClickListener { deviceLocation }
        button!!.setOnClickListener {
            point = Point(mapView!!.width / 2, mapView!!.height / 2)
            val latLng = googleMap.projection.fromScreenLocation(point)
            val geocoder = Geocoder(this@MapActivity, locale)
            val intent = Intent()
            intent.putExtra("Latitude", latLng.latitude)
            intent.putExtra("Longitude", latLng.longitude)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        map = googleMap
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        map!!.isMyLocationEnabled = true
        map!!.uiSettings.isMyLocationButtonEnabled = false
        if (mapView != null && mapView!!.findViewById<View?>("1".toInt()) != null) {
            val locationButton = (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById<View>("2".toInt())
            val layoutParams = locationButton.layoutParams as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            layoutParams.setMargins(0, 0, 40, 120)
        }
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val tast = settingsClient.checkLocationSettings(builder.build())
        tast.addOnSuccessListener(this) { locationSettingsResponse: LocationSettingsResponse? -> deviceLocation }
        tast.addOnFailureListener(this) { e: Exception? ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this@MapActivity, 51)
                } catch (e1: SendIntentException) {
                    e1.printStackTrace()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 51) {
            if (resultCode == Activity.RESULT_OK) {
                deviceLocation
            }
        }
    }

    private val deviceLocation: Unit
        private get() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    location = task.result
                    if (location != null) {
                        map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location!!.latitude, location!!.longitude), Zoom))
                    } else {
                        val locationRequest = LocationRequest.create()
                        locationRequest!!.interval = 10000
                        locationRequest.fastestInterval = 5000
                        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        locationCallback = object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult) {
                                super.onLocationResult(locationResult)
                                if (locationRequest == null) {
                                    return
                                }
                                location = locationResult.lastLocation
                                map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location?.getLatitude()!!, location?.getLongitude()!!), Zoom))
                                fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
                            }
                        }
                        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, null)
                    }
                } else {
                    Toast.makeText(this@MapActivity, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }

}