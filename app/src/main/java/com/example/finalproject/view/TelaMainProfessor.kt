package com.example.finalproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaMainProfessorBinding

class TelaMainProfessor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaMainProfessorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val disciplina = data?.getString("disciplina")

        binding.txtOla.text = "Olá $nome"
        binding.txtDisciplina.text = "Disciplina: $disciplina"
    }
}