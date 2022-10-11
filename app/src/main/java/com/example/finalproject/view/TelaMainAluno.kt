package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaMainAlunoBinding

class TelaMainAluno : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaMainAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("RA")
        val email = data?.getString("email")

        binding.txtOla.text = "Olá $nome"
        binding.txtDisciplina.text = "Turma: $turma"

        binding.imgAccount.setOnClickListener {
            val intent = Intent(this, TelaOpcAluno::class.java)
            intent.putExtra("nome", nome)
            intent.putExtra("sob", sob)
            intent.putExtra("turma", turma)
            intent.putExtra("email", email)
            intent.putExtra("RA", ra)
            startActivity(intent);
        }
    }
}