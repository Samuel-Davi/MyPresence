package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityTelaOnBoarding1Binding

class TelaOnBoarding1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOnBoarding1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btPular.setOnClickListener {
            startActivity(Intent(this,TelaEscolha::class.java))
        }
        binding.btProx.setOnClickListener {
            startActivity(Intent(this, TelaOnBoarding2::class.java))
        }
    }
}