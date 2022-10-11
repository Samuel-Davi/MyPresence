package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaCadTurmaBinding

class TelaCadTurma : AppCompatActivity() {
    private lateinit var spinnerCurso: Spinner
    private lateinit var spinnerTurma: Spinner
    private lateinit var spinnerSerie: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaCadTurmaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cursos = arrayOf("Eletrônica", "Informática", "Mecatrônica")
        var series = arrayOf("1° Série", "2° Série", "3° Série")
        var turmas = arrayOf("A", "B", "C")

        val data = intent.extras
        val inst = data?.getString("inst")
        val nome = data?.getString("nome")
        val email = data?.getString("email")

        spinnerCurso = findViewById(R.id.spinnerCursoTurma)
        spinnerCurso.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursos)
        spinnerCurso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinnerSerie = findViewById(R.id.spinnerSerieTurma)
        spinnerSerie.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, series)
        spinnerSerie.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinnerTurma = findViewById(R.id.spinnerTurma)
        spinnerTurma.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, turmas)
        spinnerTurma.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btBack.setOnClickListener {
            val intent = Intent(this, TelaAdmProf::class.java)
            intent.putExtra("inst", inst)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
    }
}