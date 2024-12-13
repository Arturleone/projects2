package com.example.databaseapp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.databaseapp.databinding.FragmentSharedPreferencesBinding

class SharedPreferencesFragment : Fragment() {

    private var _binding: FragmentSharedPreferencesBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSharedPreferencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE)
        inicializarCredenciaisPadrao()

        binding.acessarBtnMain.setOnClickListener {
            val inputUsername = binding.username.text.toString()
            val inputPassword = binding.password.text.toString()
            acessarLogin(inputUsername, inputPassword)
        }

        binding.alterarBtnMain.setOnClickListener {
            val inputUsername = binding.username.text.toString()
            val inputPassword = binding.password.text.toString()
            handleAtualizarCredenciais(inputUsername, inputPassword)
        }
    }

    private fun inicializarCredenciaisPadrao() {
        val editor = sharedPreferences.edit()

        val defaultCredentials = mapOf(
            "user" to "admin",
            "pass" to "1q2w3e",
            "user2" to "kris",
            "pass2" to "123",
            "user3" to "artur",
            "pass3" to "1234",
            "user4" to "Anabea",
            "pass4" to "12345"
        )

        defaultCredentials.forEach { (key, value) ->
            if (!sharedPreferences.contains(key)) {
                editor.putString(key, value)
            }
        }
        editor.apply()
    }

    private fun acessarLogin(username: String, password: String) {
        if (username.isEmpty()) {
            binding.usernameLayoutInput.error = "Username is required"
        } else if (password.isEmpty()) {
            binding.passwordLayoutInput.error = "Password is required"
        } else if (validarLogin(username, password)) {
            exibirMensagem("Login Successful")
        } else {
            exibirMensagem("Login Failed")
        }
    }

    private fun validarLogin(username: String, password: String): Boolean {
        for (key in sharedPreferences.all.keys) {
            if (key.startsWith("user")) {
                val savedUsername = sharedPreferences.getString(key, null)
                val savedPassword = sharedPreferences.getString(key.replace("user", "pass"), null)
                if (username == savedUsername && password == savedPassword) {
                    return true
                }
            }
        }
        return false
    }

    private fun handleAtualizarCredenciais(username: String, password: String) {
        if (username.isEmpty()) {
            binding.usernameLayoutInput.error = "Username is required"
        } else if (password.isEmpty()) {
            binding.passwordLayoutInput.error = "Password is required"
        } else {
            atualizarCredenciais(username, password)
            exibirMensagem("Credenciais Atualizadas")
        }
    }

    private fun atualizarCredenciais(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user", username)
        editor.putString("pass", password)
        editor.apply()
    }

    private fun exibirMensagem(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
