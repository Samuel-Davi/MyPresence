package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaLoginProfBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.ProfModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelaLoginProf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaLoginProfBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.botaoLogin.setOnClickListener {
            if(binding.textEmail.text.isNotEmpty() && binding.textPassword.text.isNotEmpty()){
                validaLogin(binding.textEmail.text.toString(), binding.textPassword.text.toString())
            }else{
                Toast.makeText(this@TelaLoginProf, "Preencha os campos", Toast.LENGTH_LONG).show()
            }
            binding.textPassword.text.clear()
            binding.textEmail.text.clear()
        }
    }
    private fun validaLogin(email:String, senha:String){
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.authProf(email, senha)
        callback.enqueue(object : Callback<ProfModel> {
            override fun onResponse(
                call: Call<ProfModel>,
                response: Response<ProfModel>
            ) { try {
                val prof: ProfModel = response.body()!!
                val intent = Intent(this@TelaLoginProf, TelaMainProfessor::class.java)
                intent.putExtra("nome", prof.nome)
                intent.putExtra("disciplina", prof.disciplina)
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(this@TelaLoginProf, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
            }}

            override fun onFailure(
                call: Call<ProfModel>,
                t: Throwable
            ) {Toast.makeText(this@TelaLoginProf, "Erro na conexão", Toast.LENGTH_LONG).show();
                Log.d("erro", t.message.toString())
            }
        })
    }
}