package com.example.finalproject.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.putString
import android.util.Log
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaLoginAdmBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class TelaLoginAdm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaLoginAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.txtCriaConta.setOnClickListener {
            startActivity(Intent(this, TelaCadAdm::class.java))
        }
        binding.botaoLogin.setOnClickListener {
            if(binding.textPassword.text.isNotEmpty() && binding.textemail.text.isNotEmpty()){
                validaLogin(binding.textemail.text.toString(), binding.textPassword.text.toString())
            }else{
                Toast.makeText(this@TelaLoginAdm, "Preencha os campos", Toast.LENGTH_LONG).show()
            }
            binding.textemail.text.clear()
            binding.textPassword.text.clear()
        }
    }
    private fun validaLogin(email:String, senha:String){
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.authAdm(email, senha)
        callback.enqueue(object : Callback<AdmModel> {
            override fun onResponse(
                call: Call<AdmModel>,
                response: Response<AdmModel>
            ) { try {
                val adm:AdmModel = response.body()!!
                val intent = Intent(this@TelaLoginAdm, TelaAdmProf::class.java)
                intent.putExtra("nome", adm.nome)
                intent.putExtra("email", adm.email)
                intent.putExtra("senha", adm.senha)
                intent.putExtra("instituicao", adm.instituicao)
                startActivity(intent)
            }catch (e:Exception){
                Toast.makeText(this@TelaLoginAdm, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
            }}

            override fun onFailure(
                call: Call<AdmModel>,
                t: Throwable
            ) {Toast.makeText(this@TelaLoginAdm, "Erro na conexão", Toast.LENGTH_LONG).show();
                Log.d("erro", t.message.toString())
            }
        })
    }
}
