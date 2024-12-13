package com.example.projetodeteste

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TaskActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: ArrayAdapter<String>
    private val taskList = mutableListOf<String>()
    private val taskIds = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://projeto-de-teste-706ed-default-rtdb.firebaseio.com/")
            .reference

        val etTaskTitle = findViewById<EditText>(R.id.etTaskTitle)
        val btnSaveTask = findViewById<Button>(R.id.btnSaveTask)
        taskListView = findViewById(R.id.taskListView)

        taskAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        taskListView.adapter = taskAdapter

        // Adicionar tarefa
        btnSaveTask.setOnClickListener {
            val userId = auth.currentUser?.uid ?: return@setOnClickListener
            val taskTitle = etTaskTitle.text.toString()
            val userEmail = auth.currentUser?.email ?: return@setOnClickListener

            if (taskTitle.isBlank()) {
                Toast.makeText(this, "Por favor, insira um título para a tarefa.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskId = database.child("tasks").child(userEmail.replace(".", "_")).push().key ?: return@setOnClickListener
            val task = mapOf("completed" to false, "title" to taskTitle)

            database.child("tasks").child(userEmail.replace(".", "_")).child(taskId).setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show()
                    etTaskTitle.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao salvar tarefa: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Carregar tarefas
        loadTasks()

        // Alterar status da tarefa
        taskListView.setOnItemClickListener { _, _, position, _ ->
            val userId = auth.currentUser?.uid ?: return@setOnItemClickListener
            val taskId = taskIds[position]
            val taskRef = database.child("tasks").child(userId).child(taskId)

            taskRef.child("completed").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentStatus = snapshot.getValue(Boolean::class.java) ?: false
                    taskRef.child("completed").setValue(!currentStatus)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@TaskActivity, "Erro ao alterar tarefa: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun loadTasks() {
        val userEmail = auth.currentUser?.email?.replace(".", "_") ?: return

        // Buscar tarefas do usuário usando o e-mail no lugar do UID
        database.child("tasks").child(userEmail).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                taskIds.clear()

                for (taskSnapshot in snapshot.children) {
                    val taskTitle = taskSnapshot.child("title").getValue(String::class.java) ?: ""
                    val isCompleted = taskSnapshot.child("completed").getValue(Boolean::class.java) ?: false
                    taskIds.add(taskSnapshot.key ?: "")

                    val status = if (isCompleted) "[Concluído]" else "[Pendente]"
                    taskList.add("$status $taskTitle")
                }
                taskAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@TaskActivity, "Erro ao carregar tarefas: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}