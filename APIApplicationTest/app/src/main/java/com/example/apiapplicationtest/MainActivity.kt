package com.example.apiapplicationtest

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnAddUser = findViewById(R.id.btnAddUser)

        recyclerView.layoutManager = LinearLayoutManager(this)

        Log.d("MainActivity", "Iniciando a fetchUsers para carregar os usuários iniciais.")
        fetchUsers()

        btnAddUser.setOnClickListener {
            Log.d("MainActivity", "Botão 'Adicionar Usuário' clicado.")
            showAddUserDialog()
        }
    }

    private fun fetchUsers() {
        Log.d("MainActivity", "Iniciando chamada para API para buscar usuários.")
        RetrofitInstance.api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    Log.d("MainActivity", "Usuários recebidos com sucesso: $users")
                    recyclerView.adapter = UserAdapter(users ?: emptyList())
                } else {
                    Log.e("MainActivity", "Erro ao buscar usuários: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.e("MainActivity", "Falha ao buscar usuários", t)
            }
        })
    }

    private fun showAddUserDialog() {
        Log.d("MainActivity", "Abrindo diálogo para adicionar um novo usuário.")
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_user, null)
        val etName = dialogView.findViewById<EditText>(R.id.etName)
        val etUsername = dialogView.findViewById<EditText>(R.id.etUsername)
        val etEmail = dialogView.findViewById<EditText>(R.id.etEmail)
        val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()

            Log.d("MainActivity", "Dados inseridos no formulário: Nome=$name, Username=$username, Email=$email")

            if (name.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty()) {
                val newUser = User(0, name, username, email)
                createUser(newUser)
                dialog.dismiss()
            } else {
                Log.e("MainActivity", "Erro: Todos os campos precisam estar preenchidos.")
            }
        }

        dialog.show()
    }

    private fun createUser(user: User) {
        Log.d("MainActivity", "Enviando novo usuário para API: $user")
        RetrofitInstance.api.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("MainActivity", "Usuário criado com sucesso: ${response.body()}")
                    fetchUsers()
                } else {
                    Log.e("MainActivity", "Erro ao criar usuário: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("MainActivity", "Falha ao criar usuário", t)
            }
        })
    }
}
