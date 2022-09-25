package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaOnBoarding3Binding

class TelaOnBoarding3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOnBoarding3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btProx.setOnClickListener {
            startActivity(Intent(this, TelaEscolha::class.java))
        }
        binding.btVoltar.setOnClickListener {
            startActivity(Intent(this, TelaOnBoarding2::class.java))
        }
    }
}