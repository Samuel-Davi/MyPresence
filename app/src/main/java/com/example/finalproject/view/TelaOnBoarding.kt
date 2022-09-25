package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.databinding.ActivityTelaOnBoardingBinding

class TelaOnBoarding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root);

        binding.btProx.setOnClickListener {
            startActivity(Intent(this, TelaOnBoarding1::class.java));
        }
    }
}