package com.example.finalproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.ActivityTelaAdmProfBinding
import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.ProfModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import com.example.finalproject.recycler.ProfAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TelaAdmProf : AppCompatActivity() {

    private lateinit var profs: ArrayList<ProfModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaAdmProfBinding.inflate(layoutInflater)
        var valorClick:Boolean = false;
        setContentView(binding.root)

        //getProfs(profs)

        val data = intent.extras
        val nome = data?.getString("nome")
        val senha = data?.getString("senha")
        val instituicao = data?.getString("instituicao")
        val email = data?.getString("email")

        binding.txtOla.text = "Olá $nome";


        binding.imgAccount.setOnClickListener {
            val intent = Intent(this, TelaOpcAdm::class.java)
            intent.putExtra("senha", senha)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }

        binding.txtTurma.setOnClickListener {
            val intent = Intent(this, TelaAdmTurmas::class.java)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
        binding.imgAdd.setOnClickListener {
            if(!valorClick){
                binding.btCadProf.isVisible = true
                binding.btCadTurma.isVisible = true
                valorClick = !valorClick
            }else{
                binding.btCadProf.isVisible = false
                binding.btCadTurma.isVisible = false
                valorClick = !valorClick
            }
        }
        binding.btCadProf.setOnClickListener {
            startActivity(Intent(this, TelaCadProf::class.java))
        }
        binding.btCadTurma.setOnClickListener {
            startActivity(Intent(this, TelaCadTurma::class.java))
        }
    }

    private fun getProfs(users: ArrayList<ProfModel>){
        val prof: ProfModel? = null
        val endpoint = ApiClient.getRetrofit().create(InterfaceApi::class.java)
        val callback = prof?.let { endpoint.getProfs(it.email, prof.nome, prof.senha, prof.disciplina) }
        callback?.enqueue(object : Callback<List<ProfModel>>{
            override fun onResponse(
                call: Call<List<ProfModel>>,
                response: Response<List<ProfModel>>
            ) {
                Log.d("data", response.body()!!.toString())
            }

            override fun onFailure(call: Call<List<ProfModel>>, t: Throwable) {
                Log.d("erro", t.message.toString())
            }

        })
    }
}