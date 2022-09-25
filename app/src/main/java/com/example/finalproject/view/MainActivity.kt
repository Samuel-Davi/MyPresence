package com.example.finalproject.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        var handler = Handler()
        handler.postDelayed(Runnable {
              startActivity(Intent(this, TelaOnBoarding::class.java))
              finish();
        }
            , 2000)

    }
}