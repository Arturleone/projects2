package com.example.googlemaps_api_wsk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var startLocationEditText: EditText
    private lateinit var endLocationEditText: EditText
    private lateinit var routeButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startLocationEditText = findViewById(R.id.startLocation)
        endLocationEditText = findViewById(R.id.endLocation)
        routeButton = findViewById(R.id.routeButton)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Clique do botão para buscar a rota
        routeButton.setOnClickListener {
            val origin = startLocationEditText.text.toString()
            val destination = endLocationEditText.text.toString()

            Log.d("MainActivity", "onClick: Origin = $origin, Destination = $destination")

            if (origin.isNotEmpty() && destination.isNotEmpty()) {
                fetchRoute(origin, destination)
            } else {
                Log.d("MainActivity", "onClick: Origin or destination is empty")
            }
        }

        // Obtém a referência do fragmento do mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        Log.d("MainActivity", "onCreate: Map fragment initialized")
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("MainActivity", "onMapReady: Map is ready")

        // Verificar permissões de localização
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Solicitar permissões se não forem concedidas
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        // Ativar a camada de localização do mapa
        mMap.isMyLocationEnabled = true

        // Obter a localização atual do usuário e centralizar o mapa
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
                    Log.d("MainActivity", "onMapReady: User's location is $userLocation")
                } else {
                    Toast.makeText(this, "Não foi possível obter sua localização.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fetchRoute(origin: String, destination: String) {
        val apiKey = "AIzaSyBE24TCqY7jQ6eOaJWUGlXLrwIKgTT9sw4"  // Sua chave da API
        val url = "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=$apiKey"

        Log.d("MainActivity", "fetchRoute: Making request to $url")

        // Criação do cliente HTTP
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        // Enviar a requisição de forma assíncrona
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "fetchRoute: Error while making request", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("MainActivity", "fetchRoute: Response received: $responseBody")
                    if (responseBody != null) {
                        parseAndDrawRoute(responseBody)
                    }
                } else {
                    Log.e("MainActivity", "fetchRoute: Request failed with code: ${response.code}")
                }
            }
        })
    }

    private fun parseAndDrawRoute(responseBody: String) {
        Log.d("MainActivity", "parseAndDrawRoute: Parsing response")
        val jsonObject = JSONObject(responseBody)
        val routes = jsonObject.getJSONArray("routes")
        Log.d("MainActivity", "parseAndDrawRoute: Routes array length = ${routes.length()}")
        val points = mutableListOf<LatLng>()

        // Itera sobre as rotas
        for (i in 0 until routes.length()) {
            Log.d("MainActivity", "parseAndDrawRoute: Processing route $i")
            val route = routes.getJSONObject(i)
            val legs = route.getJSONArray("legs")
            Log.d("MainActivity", "parseAndDrawRoute: Legs array length = ${legs.length()}")
            for (j in 0 until legs.length()) {
                Log.d("MainActivity", "parseAndDrawRoute: Processing leg $j")
                val leg = legs.getJSONObject(j)
                val steps = leg.getJSONArray("steps")
                Log.d("MainActivity", "parseAndDrawRoute: Steps array length = ${steps.length()}")
                for (k in 0 until steps.length()) {
                    Log.d("MainActivity", "parseAndDrawRoute: Processing step $k")
                    val step = steps.getJSONObject(k)
                    val polyline = step.getJSONObject("polyline")
                    val pointsString = polyline.getString("points")
                    Log.d("MainActivity", "parseAndDrawRoute: Polyline points = $pointsString")
                    val decodedPoints = decodePoly(pointsString)
                    points.addAll(decodedPoints)
                }
            }
        }

        // Atualiza a UI na thread principal
        runOnUiThread {
            Log.d("MainActivity", "parseAndDrawRoute: Drawing polyline on the map")
            val polylineOptions = PolylineOptions().addAll(points)
            mMap.addPolyline(polylineOptions)
            if (points.isNotEmpty()) {
                val firstPoint = points[0]
                Log.d("MainActivity", "parseAndDrawRoute: Moving camera to $firstPoint")
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPoint, 14f))
            }
        }
    }

    // Método para decodificar a polyline
    private fun decodePoly(encoded: String): List<LatLng> {
        Log.d("MainActivity", "decodePoly: Decoding polyline")
        val poly = mutableListOf<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        // Decodifica a polyline
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            poly.add(LatLng(lat / 1E5, lng / 1E5))
        }

        Log.d("MainActivity", "decodePoly: Decoded polyline size = ${poly.size}")
        return poly
    }
}
