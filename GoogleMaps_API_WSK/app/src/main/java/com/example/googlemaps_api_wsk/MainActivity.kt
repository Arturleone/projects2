package com.example.googlemaps_api_wsk

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var originInput: EditText
    private lateinit var destinationInput: EditText
    private lateinit var btnRoute: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        originInput = findViewById(R.id.origin)
        destinationInput = findViewById(R.id.destination)
        btnRoute = findViewById(R.id.btnRoute)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnRoute.setOnClickListener {
            val origin = originInput.text.toString()
            val destination = destinationInput.text.toString()

            if (origin.isNotEmpty() && destination.isNotEmpty()) {
                getRoute(origin, destination)
            } else {
                Toast.makeText(this, "Preencha os campos de origem e destino", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
    }

    private fun getRoute(origin: String, destination: String) {
        val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=$origin&destination=$destination&key=SUA_API_KEY_AQUI"

        Thread {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string() ?: "")

            val routes = json.getJSONArray("routes")
            if (routes.length() > 0) {
                val points = routes.getJSONObject(0)
                    .getJSONObject("overview_polyline")
                    .getString("points")
                val path = PolyUtil.decode(points)

                runOnUiThread {
                    map.clear()
                    map.addPolyline(PolylineOptions().addAll(path))
                    map.addMarker(MarkerOptions().position(path.first()).title("Origem"))
                    map.addMarker(MarkerOptions().position(path.last()).title("Destino"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(path.first(), 14f))
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Rota não encontrada", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}