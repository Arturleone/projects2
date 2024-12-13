package com.example.api_wsc2023.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.api_wsc2023.API.RetrofitClient
import com.example.api_wsc2023.R
import com.example.api_wsc2023.DateClasses.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        // Inicializando e atribuindo diretamente as views
        val etNome: EditText = findViewById(R.id.etNome)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etSenha: EditText = findViewById(R.id.etSenha)
        val etDr: EditText = findViewById(R.id.etDr)
        val etTipoUsuarioId: EditText = findViewById(R.id.etTipoUsuarioId)
        val btnCadastrar: Button = findViewById(R.id.btnCadastrar)
        val btnVoltarLogin: Button = findViewById(R.id.btnVoltarLogin)

        btnCadastrar.setOnClickListener {
            val nome = etNome.text.toString()
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()
            val dr = etDr.text.toString()
            val tipoUsuarioId = etTipoUsuarioId.text.toString().toIntOrNull() ?: 0

            val usuario = Usuario(nome, email, senha, dr, tipoUsuarioId)

            cadastrarUsuario(usuario)
        }

        btnVoltarLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cadastrarUsuario(usuario: Usuario) {
        RetrofitClient.usuarioApi.cadastrarUsuario(usuario).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastroActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CadastroActivity, "Erro no cadastro: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@CadastroActivity, "Erro na comunicação com a API: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("CadastroActivity", "Erro na comunicação: ${t.message}", t)
            }
        })
    }
}
