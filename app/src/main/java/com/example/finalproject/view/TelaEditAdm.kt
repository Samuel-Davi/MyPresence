package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaEditAdmBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.UserModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelaEditAdm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaEditAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val email = data?.getString("email")
        val senha = data?.getString("senha")

        binding.btAlterar.setOnClickListener {
            updateAdm(binding.textemail.text.toString(), binding.textSenha.text.toString())
        }
    }
    private fun updateAdm(email:String, senha:String){
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.updateUser(email, senha)
        callback.enqueue(object : Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) { try {
                val intent = Intent(this@TelaEditAdm, TelaOpcAdm::class.java)
                startActivity(intent)
            }catch (e:Exception){
                Log.d("e", e.message.toString())
                Toast.makeText(this@TelaEditAdm, "Email incorreto", Toast.LENGTH_LONG).show()
            }}

            override fun onFailure(
                call: Call<UserModel>,
                t: Throwable
            ) {
                Toast.makeText(this@TelaEditAdm, "falha na conexão", Toast.LENGTH_LONG).show();
                Log.d("erro", t.message.toString())
            }
        })
    }
}