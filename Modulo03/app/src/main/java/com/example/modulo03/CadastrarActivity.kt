package com.example.modulo03

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat.Callback
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Response

class CadastrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastrar)

        val name = findViewById<EditText>(R.id.nome)
        val email = findViewById<EditText>(R.id.email_usuario)
        val password = findViewById<EditText>(R.id.senha_cadastro)
        val confirmPassword = findViewById<EditText>(R.id.confirmar_senha_cadastro)
        val buttonCadastrar = findViewById<Button>(R.id.botao_cadastrar_cadastro)
        val iconVoltar = findViewById<ImageView>(R.id.icon_voltar)
        iconVoltar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        buttonCadastrar.setOnClickListener {
            val inputName = name.text.toString()
            val inputEmail = email.text.toString()
            val inputPassword = password.text.toString()
            val inputConfirmPassword = confirmPassword.text.toString()

            if (inputName.isBlank() || inputEmail.isBlank() || inputPassword.isBlank() || inputConfirmPassword.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                val usuario = Usuario(inputName, inputEmail, inputPassword, "MG", 1)
                cadastrarUsuario(usuario)
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun cadastrarUsuario(usuario: Usuario) {
        RetrofitClient.usuarioApi.cadastrarUsuario(usuario).enqueue(object :
            retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CadastrarActivity, "Usuário cadastrado com sucesso!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@CadastrarActivity, "Erro no cadastro: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }

}