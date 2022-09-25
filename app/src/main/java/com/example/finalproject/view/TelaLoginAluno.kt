package com.example.finalproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityTelaLoginAlunoBinding

class TelaLoginAluno : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaLoginAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}