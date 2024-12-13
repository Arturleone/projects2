package com.example.coroutinesthreadsworkmanager

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Constraints
import androidx.work.NetworkType
import android.content.Context
import android.os.SystemClock
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)
        startButton = findViewById(R.id.startButton)

        startButton.setOnClickListener {
            runBlocking {
                Log.d("MainActivity", "Botão pressionado para iniciar a tarefa.")
                startCoroutineTask()
                // startThreadTask()
                // startWorkManagerTask()
            }
        }
    }


//
    private suspend fun startCoroutineTask() {
        Log.d("MainActivity", "Iniciando contagem regressiva com Coroutine...")
        statusTextView.text = "Iniciando contagem regressiva com Coroutine..."

        // Simulando a contagem regressiva
        for (i in 10 downTo 1) {
            Log.d("Coroutine", "Contagem regressiva: $i")
            delay(1000) // 1 segundo de atraso
            statusTextView.text = "Contagem regressiva: $i"
        }

        statusTextView.text = "Tarefa com Coroutine concluída!"
        Log.d("Coroutine", "Tarefa com Coroutine concluída!")
    }


    //STARTTHREADTASK
    private fun startThreadTask() {
        Log.d("MainActivity", "Iniciando contagem regressiva com Thread...")
        statusTextView.text = "Iniciando contagem regressiva com Thread..."

        // Criando uma nova thread
        Thread {
            for (i in 10 downTo 1) {
                Log.d("Thread", "Contagem regressiva: $i")
                Thread.sleep(1000) // 1 segundo de espera
                runOnUiThread {
                    statusTextView.text = "Contagem regressiva: $i"
                }
            }

            runOnUiThread {
                statusTextView.text = "Tarefa com Thread concluída!"
            }
            Log.d("Thread", "Tarefa com Thread concluída!")
        }.start()
    }


    //WORKMANAGERTASK
    private fun startWorkManagerTask() {
        Log.d("MainActivity", "Iniciando contagem regressiva com WorkManager...")
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED) // Não precisa de rede
            .build()

        val workRequest: WorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)

        statusTextView.text = "Contagem regressiva com WorkManager iniciada..."
    }


    class MyWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {

        override fun doWork(): Result {
            Log.d("WorkManager", "Iniciando contagem regressiva com WorkManager...")

            // Simulando contagem regressiva de 5 segundos
            for (i in 10 downTo 1) {
                SystemClock.sleep(1000) // Atraso de 1 segundo
                Log.d("WorkManager", "Contagem regressiva: $i")
            }

            // Retorna sucesso após a tarefa
            Log.d("WorkManager", "Tarefa com WorkManager concluída!")
            return Result.success()
        }
    }
}
