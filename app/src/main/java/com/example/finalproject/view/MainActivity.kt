package com.example.finalproject.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityMainBinding
import org.opencv.android.OpenCVLoader

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