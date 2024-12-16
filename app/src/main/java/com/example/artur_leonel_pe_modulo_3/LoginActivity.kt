package com.example.artur_leonel_pe_modulo_3

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private var attemptCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val sharedPreferences: SharedPreferences = getSharedPreferences("PREFS", MODE_PRIVATE)
        val botaoAcessar = findViewById<Button>(R.id.acessar_login)
        val botaoCadastro = findViewById<Button>(R.id.cadastrar_login)
        val userName = findViewById<EditText>(R.id.username_acessar)
        val password = findViewById<EditText>(R.id.password_login)

        val backgroundRed = ContextCompat.getDrawable(this, R.drawable.borderred)
        val backgroundDefault = ContextCompat.getDrawable(this, R.drawable.borderblack)

        botaoCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        botaoAcessar.setOnClickListener {
            val inputUsername = userName.text.toString()
            val inputPassword = password.text.toString()

            if (inputUsername.isNotBlank() && inputPassword.isNotBlank()) {
                realizarLogin(inputUsername, inputPassword)

            } else {
                messageShowBlock("Dados incorretos!")
                userName.background = backgroundRed
                password.background = backgroundRed
                attemptCount++

                Handler(Looper.getMainLooper()).postDelayed({
                    if (attemptCount <= 3) {
                        userName.background = backgroundDefault
                        password.background = backgroundDefault
                    }
                }, 750)

                if (attemptCount > 3) {
                    bloquearLogin(userName, password, botaoAcessar, backgroundDefault)
                }
            }
        }
    }

    private fun bloquearLogin(userName: EditText, password: EditText, botaoAcessar: Button, backgroundDefault: Drawable?) {
        messageShowBlock("Login bloqueado: aguarde 30s!")
        userName.isEnabled = false
        password.isEnabled = false
        botaoAcessar.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            userName.background = backgroundDefault
            password.background = backgroundDefault
            userName.isEnabled = true
            password.isEnabled = true
            botaoAcessar.isEnabled = true
            attemptCount = 0
        }, 30000) // 30 segundos
    }

    private fun messageShowBlock(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun realizarLogin(nome: String, senha: String) {
        // Primeiramente, vamos procurar o usuário pelo nome
        Log.d("Login", "Buscando usuário pelo nome: $nome")
        listarUsuarios(nome, senha)
    }

    private fun listarUsuarios(nome: String, senha: String) {
        val call = RetrofitClient.usuarioApi.listarUsuarios()
        call.enqueue(object : Callback<List<UserDetailsResponse>> {
            override fun onResponse(
                call: Call<List<UserDetailsResponse>>,
                response: Response<List<UserDetailsResponse>>
            ) {
                Log.d("Login", "Resposta da API para listar usuários: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    val usuarios = response.body() ?: emptyList()
                    val usuario = usuarios.find { it.nome == nome } // Procura o nome

                    if (usuario != null) {
                        val email = usuario.email // Pega o email do usuário encontrado
                        Log.d("Login", "Usuário encontrado: $nome, Email: $email")
                        realizarLoginComEmail(email, senha) // Realiza o login com o email
                    } else {
                        Log.d("Login", "Usuário não encontrado: $nome")
                        Toast.makeText(this@LoginActivity, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("Login", "Erro ao listar usuários, código: ${response.code()}")
                    Toast.makeText(this@LoginActivity, "Erro ao listar usuários.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserDetailsResponse>>, t: Throwable) {
                Log.d("Login", "Erro ao buscar usuários: ${t.message}")
                Toast.makeText(this@LoginActivity, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun realizarLoginComEmail(email: String, senha: String) {
        Log.d("Login", "Tentando login com Email: $email e Senha: $senha")
        val call = RetrofitClient.usuarioApi.realizarLogin(email, senha)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Log para ver o código de status e corpo da resposta
                Log.d("Login", "Código de status: ${response.code()}")
                Log.d("Login", "Corpo da resposta: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    // Sucesso no login, redireciona para a tela principal
                    val usuario = response.body()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("EMAIL", email)
                    startActivity(intent)
                    finish()
                } else {
                    // Caso a resposta não seja bem-sucedida, logamos o erro
                    Log.d("Login", "Erro ao realizar login com email: $email")
                    Toast.makeText(
                        this@LoginActivity,
                        "Erro ao realizar login. Código de erro: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("Login", "Erro de rede ao tentar login: ${t.message}")
                Toast.makeText(this@LoginActivity, "Erro de rede: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}
