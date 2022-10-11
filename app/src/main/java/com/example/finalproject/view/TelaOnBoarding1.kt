package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.fragment.FragmentOnBoarding1
import com.example.finalproject.fragment.FragmentOnBoarding2
import com.example.finalproject.fragment.FragmentOnBoarding3

class TelaOnBoarding1 : AppCompatActivity() , View.OnClickListener {

    private lateinit var buttonProx:TextView
    private lateinit var buttonBack: TextView
    var valor = 1;

    private lateinit var firstFragment: FragmentOnBoarding1
    private lateinit var secondFragment: FragmentOnBoarding2
    private lateinit var thirdFragment: FragmentOnBoarding3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_on_boarding1)

        buttonProx = findViewById(R.id.btProx)
        buttonProx.setOnClickListener(this)

        buttonBack = findViewById(R.id.btPular)
        buttonBack.setOnClickListener(this)

        firstFragment = FragmentOnBoarding1()
        secondFragment = FragmentOnBoarding2()
        thirdFragment = FragmentOnBoarding3()

        val fragmentTransaction =
        supportFragmentManager.beginTransaction().add(R.id.fragments, firstFragment)
        fragmentTransaction.commit()

    }

    private fun replaceFragment(fragment:Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View) {
        val image:ImageView = findViewById(R.id.imgbolinha)
        when(v.id){
            R.id.btProx ->{
                when (valor){
                    1 ->{
                        replaceFragment(secondFragment)
                        image.setImageDrawable(getDrawable(R.drawable.bolinhaonboarding2))
                        buttonBack = findViewById(R.id.btPular)
                        buttonBack.text = "Voltar"
                        valor += 1
                    }
                    2 ->{
                        replaceFragment(thirdFragment)
                        image.setImageDrawable(getDrawable(R.drawable.bolinhaonboarding3))
                        valor += 1
                    }
                    3 ->{
                        startActivity(Intent(this, TelaEscolha::class.java))
                    }
                }
            }
            R.id.btPular ->{
                when(valor){
                    1 ->{
                        startActivity(Intent(this, TelaEscolha::class.java))
                    }
                    2 ->{
                        replaceFragment(firstFragment)
                        image.setImageDrawable(getDrawable(R.drawable.bolinhaonboarding1))
                        buttonBack.text = "Pular"
                        valor -= 1
                    }
                    3 ->{
                        replaceFragment(secondFragment)
                        image.setImageDrawable(getDrawable(R.drawable.bolinhaonboarding2))
                        valor -= 1
                    }
                }
            }
        }
    }
}