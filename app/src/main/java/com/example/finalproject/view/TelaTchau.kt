package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaTchauBinding

class TelaTchau : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaTchauBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btInit.setOnClickListener {
            startActivity(Intent(this, TelaEscolha::class.java))
        }
    }
}