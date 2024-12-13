package com.example.youtube

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private lateinit var rv_main: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var motion_container: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializando os elementos da tela
        motion_container = findViewById(R.id.motion_container)
        rv_main = findViewById(R.id.rv_main)

        val videos = mutableListOf<com.example.youtube.Video>()
        videoAdapter = VideoAdapter(videos) { video: com.example.youtube.Video ->
            println(video)
        }

        rv_main.layoutManager = LinearLayoutManager(this)
        rv_main.adapter = videoAdapter

        // Carregar os vídeos com corrotinas
        CoroutineScope(Dispatchers.IO).launch {
            val res = async { getVideo() }
            val listVideo = res.await()
            withContext(Dispatchers.Main) {
                listVideo?.let {
                    videos.clear()
                    videos.addAll(listVideo.data)
                    videoAdapter.notifyDataSetChanged()
                    // Remover o progresso
                    motion_container.removeView(findViewById(R.id.progress_recycler))
                }
            }
        }
    }

    private fun getVideo(): ListVideo? {
        val client = OkHttpClient.Builder().build()

        val request = Request.Builder()
            .get()
            .url("https://tiagoaguiar.co/api/youtube-videos")
            .build()

        return try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                GsonBuilder().create()
                    .fromJson(response.body()?.string(), ListVideo::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
