package com.example.artur_leonel_pe_modulo_3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cadastro)

        val nameFull = findViewById<EditText>(R.id.nome_completo_cadastro)
        val emailUsuario = findViewById<EditText>(R.id.email_usuario)
        val password = findViewById<EditText>(R.id.senha_cadastro)
        val confirmPassword = findViewById<EditText>(R.id.confirmar_senha_cadastro)
        val buttonCadastrarCadastro = findViewById<Button>(R.id.botao_cadastrar_cadastro)
        val iconVoltar = findViewById<ImageView>(R.id.icon_voltar)
        iconVoltar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        buttonCadastrarCadastro.setOnClickListener {
            val inputNameFull = nameFull.text.toString()
            val inputEmail = emailUsuario.text.toString()
            val inputPassword = password.text.toString()
            val inputConfirmPassword = confirmPassword.text.toString()

            if (inputNameFull.isBlank() || inputEmail.isBlank() || inputPassword.isBlank() || inputConfirmPassword.isBlank()) {
                messageShowBlock("Preencha Todos Os Dados!")
            } else if (!verificarNomeCompleto(inputNameFull)) {
                messageShowBlock("O Nome Completo deve conter pelo menos dois nomes!")
            } else if (!validarEmail(inputEmail)) {
                emailUsuario.error = ("não pode começar com número, deve conter no mínimo 6 caracteres antes do “@”, após o “@” deve conter no mínimo 3 caracteres seguido de “.com” ou “.com.br”")
            } else if (compararPassword(inputPassword, inputConfirmPassword)) {
                messageShowBlock("As Senhas Não Conferem")
            } else if (!validarSenha(inputPassword)) {
            messageShowBlock("A senha é necessária no mínimo 8 caracteres, no mínimo dois números, uma letra maiuscula e uma letra minuscula.\n")
            } else {
                val usuario = Usuario(inputNameFull, inputEmail, inputPassword, "MG", 1)
                cadastrarUsuario(usuario)
                startActivity(Intent(this, LoginActivity::class.java))
            }
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

    fun validarEmail(email: String): Boolean {
        // Regex para validar o e-mail
        val regex = Regex("^(?![0-9])([a-z0-9_.]{6,})@([a-z0-9]{3,})(\\.com|\\.com\\.br)$")
        return regex.matches(email)
    }

    fun validarSenha(senha: String): Boolean {
        // Verifica o tamanho
        if (senha.length < 8) return false

        // Contadores para os requisitos
        val hasUpperCase = senha.count { it.isUpperCase() } > 0
        val hasLowerCase = senha.count { it.isLowerCase() } > 0
        val hasTwoDigits = senha.count { it.isDigit() } >= 2
        val hasValidCharacters = senha.all { it.isLetterOrDigit() || it == '_' || it == '.' }

        // Retorna true se todos os requisitos forem atendidos
        return hasUpperCase && hasLowerCase && hasTwoDigits && hasValidCharacters
    }

    //Mensagem toast para o usuario
    private fun messageShowBlock(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //Verificar possivel nome e sobrenome
    private fun verificarNomeCompleto(nomeCompleto: String): Boolean {
        val palavras = nomeCompleto.trim().split("\\s+".toRegex())
        return palavras.size >= 2
    }
    //comparar as Senha
    private fun compararPassword(password: String, confirmPassword: String): Boolean {
        return password != confirmPassword
    }
    //Adicionar ao SharedPreferences
}
