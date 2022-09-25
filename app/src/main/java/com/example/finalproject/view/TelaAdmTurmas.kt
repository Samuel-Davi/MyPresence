package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaAdmTurmasBinding

class TelaAdmTurmas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaAdmTurmasBinding.inflate(layoutInflater)
        var valorClick:Boolean = false;
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")

        binding.txtOla.text = "Olá $nome"

        binding.imgAccount.setOnClickListener {
            startActivity(Intent(this, TelaOpcAdm::class.java))
        }

        binding.txtProf.setOnClickListener {
            val intent = Intent(this, TelaAdmProf::class.java)
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
}