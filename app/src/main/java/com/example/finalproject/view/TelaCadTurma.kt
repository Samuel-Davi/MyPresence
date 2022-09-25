package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaCadTurmaBinding

class TelaCadTurma : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaCadTurmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBack.setOnClickListener {
            startActivity(Intent(this, TelaAdmTurmas::class.java))
        }
    }
}