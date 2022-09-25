package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaOpcAdmBinding
import com.example.finalproject.model.UserModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class TelaOpcAdm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOpcAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val email = data?.getString("email")
        val senha = data?.getString("senha")
        val nome = data?.getString("nome")

        binding.txtSair.setOnClickListener {
            val intent = Intent(this, TelaLoginAdm::class.java)
            intent.putExtra("email", email)
            intent.putExtra("senha", email)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
        binding.txtEdit.setOnClickListener {
            val intent = Intent(this, TelaEditAdm::class.java)
            intent.putExtra("email", email)
            intent.putExtra("senha", email)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
        binding.txtDel.setOnClickListener {
            binding.linearCaixaDel.isVisible = true;
            binding.txtDel.isVisible = false
        }
        binding.txtVoltar.setOnClickListener {
            binding.linearCaixaDel.isVisible = false
            binding.txtDel.isVisible = true
        }
        binding.txtExcluir.setOnClickListener {
            deleteAdm(email.toString(), senha.toString())
            binding.linearCaixaDel.isVisible = false
            binding.txtDel.isVisible = true;
        }
    }
    private fun deleteAdm(email:String, senha:String){
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = endpoint.deleteUser(email, senha)
        callback.enqueue(object : retrofit2.Callback<UserModel> {
            override fun onResponse(
                call: Call<UserModel>,
                response: Response<UserModel>
            ) { try {
                Toast.makeText(this@TelaOpcAdm, "Deletado com sucesso!", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@TelaOpcAdm, TelaLoginAdm::class.java))
            }catch (e:Exception){
                Log.d("e", e.message.toString())
                Toast.makeText(this@TelaOpcAdm, "Não foi possivel deletar", Toast.LENGTH_LONG).show()
            }}

            override fun onFailure(
                call: Call<UserModel>,
                t: Throwable
            ) {
                Toast.makeText(this@TelaOpcAdm, "falha na conexão", Toast.LENGTH_LONG).show();
                Log.d("erro", t.message.toString())
            }
        })
    }
}