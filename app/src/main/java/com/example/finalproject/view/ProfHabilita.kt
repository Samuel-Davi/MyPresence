package com.example.finalproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityProfHabilitaBinding

class ProfHabilita : AppCompatActivity() {

    private lateinit var binding:ActivityProfHabilitaBinding

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfHabilitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cursos = arrayOf("Eletrônica", "Informática", "Mecatrônica")
        var series = arrayOf("1° Série", "2° Série", "3° Série")
        var turmas = arrayOf("A", "B", "C")

        spinner1 = binding.spinnerCursoHabilitaProf
        spinner1.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursos)
    }
}