package com.example.okhttpapi

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import com.squareup.picasso.Picasso
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var btnFetchImage: Button
    private lateinit var imgDog: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnFetchImage = findViewById(R.id.btnFetchImage)
        imgDog = findViewById(R.id.imgDog)

        btnFetchImage.setOnClickListener {
            fetchRandomDogImage()
        }
    }

    private fun fetchRandomDogImage() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://dog.ceo/api/breeds/image/random")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body ?: throw IOException("Resposta vazia")
                    val json = JSONObject(responseBody.string())
                    val imageUrl = json.getString("message")

                    runOnUiThread {
                        Picasso.get().load(imageUrl).into(imgDog)
                    }
                } else {
                    showError("Erro na resposta da API")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                showError("Erro na conexão com a API")
            }
        }.start()
    }

    private fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
