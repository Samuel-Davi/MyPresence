package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaCadProfBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.ProfModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TelaCadProf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaCadProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btCad.setOnClickListener {
            if(binding.edtNome.text.isEmpty() || binding.edtSenha.text.isEmpty()
                || binding.edtEmail.text.isEmpty() || binding.edtDisc.text.isEmpty()){
                Toast.makeText(this,"Preencha os campos corretamente", Toast.LENGTH_LONG).show();
            }else{
                cadProf(binding.edtNome.text.toString(), binding.edtEmail.text.toString(),
                    binding.edtSenha.text.toString(), binding.edtDisc.text.toString());
                binding.edtNome.text.clear()
                binding.edtEmail.text.clear()
                binding.edtSenha.text.clear()
                binding.edtDisc.text.clear()
            }
        }
        binding.btBack.setOnClickListener {
            startActivity(Intent(this, TelaAdmProf::class.java))
        }
    }
    private fun cadProf(nome:String, email:String, senha:String, disciplina:String){
        val tipo = 1;
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.createProf(nome, senha, disciplina, email, tipo)
        callback.enqueue(object : retrofit2.Callback<ProfModel> {
            override fun onResponse(
                call: Call<ProfModel>,
                response: Response<ProfModel>
            ) {
                Log.d("3431321", "Chegando aq")
                Toast.makeText(this@TelaCadProf, "Cadastro Realizado", Toast.LENGTH_LONG).show()
                val intent = Intent(this@TelaCadProf, TelaAdmProf::class.java)
                startActivity(intent)
            }

            override fun onFailure(
                call: Call<ProfModel>,
                t: Throwable
            ){
                Log.d("erro", t.message.toString())
                Toast.makeText(this@TelaCadProf, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()}
        })
    }
}