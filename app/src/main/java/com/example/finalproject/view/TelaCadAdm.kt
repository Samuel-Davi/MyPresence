package com.example.finalproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityTelaCadAdmBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.ProfModel
import com.example.finalproject.model.UserModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelaCadAdm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaCadAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtJaTemConta.setOnClickListener {
            startActivity(Intent(this, TelaLoginAdm::class.java));
        }
        binding.btConfirma.setOnClickListener {
            if(binding.textEmail.text.isEmpty() || binding.textNickname.text.isEmpty() || binding.textSenha.text.isEmpty() || binding.textInstituicao.text.isEmpty()){
                Toast.makeText(this,"Preencha os campos corretamente", Toast.LENGTH_LONG).show();
            }else{
                cadAdm(binding.textNickname.text.toString(), binding.textEmail.text.toString(), binding.textSenha.text.toString(), binding.textInstituicao.text.toString());
                binding.textEmail.text.clear()
                binding.textNickname.text.clear()
                binding.textSenha.text.clear()
                binding.textInstituicao.text.clear()
            }
        }
    }
    private fun cadAdm(nome:String, email:String, senha:String, instituicao:String){
        val tipo:Int = 0;
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.createAdm(nome, senha, email, instituicao, tipo)
        callback.enqueue(object : Callback<AdmModel>{
            override fun onResponse(
                call: Call<AdmModel>,
                response: Response<AdmModel>
            ) {startActivity(Intent(this@TelaCadAdm, TelaLoginAdm::class.java))
                Toast.makeText(this@TelaCadAdm, "Cadastro Realizado", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(
                call: Call<AdmModel>,
                t: Throwable
            ) {Log.d("erro", t.message.toString()); Toast.makeText(this@TelaCadAdm, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()}
        })
    }
}
