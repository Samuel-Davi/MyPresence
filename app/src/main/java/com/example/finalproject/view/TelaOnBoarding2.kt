package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaOnBoarding2Binding

class TelaOnBoarding2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOnBoarding2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btVoltar.setOnClickListener {
            startActivity(Intent(this, TelaOnBoarding1::class.java))
        }
        binding.btProx.setOnClickListener {
            startActivity(Intent(this, TelaOnBoarding3::class.java))
        }
    }
}