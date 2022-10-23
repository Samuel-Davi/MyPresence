package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaCadDisciplinasBinding
import com.google.firebase.firestore.FirebaseFirestore

class TelaCadDisciplinas : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private lateinit var spinner2:Spinner
    private lateinit var spinner3:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaCadDisciplinasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var cursos = arrayOf("Todos", "Eletrônica", "Informática", "Mecatrônica")
        var series = arrayOf("Todas", "1° Série", "2° Série", "3° Série")
        var cursoSelecionado = ""
        var serieSelecionada = ""

        spinner2 = binding.spinner2
        spinner2.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cursos)
        spinner2.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cursoSelecionado = cursos[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinner3 = binding.spinner3
        spinner3.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, series)
        spinner3.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                serieSelecionada = series[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val data = intent.extras
        val nome = data?.getString("nome")
        val inst = data?.getString("inst")
        val email = data?.getString("email")

        with(binding){
            btVoltar.setOnClickListener {
                val intent = Intent(this@TelaCadDisciplinas, TelaAdmProf::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("inst", inst)
                intent.putExtra("email", email)
                startActivity(intent)
            }

            layoutFinalizar.setOnClickListener {
                var nomeDisciplina = edtNomeDisciplina.text.toString()
                cadDisciplina(nome.toString(), inst.toString(), email.toString(), cursoSelecionado, serieSelecionada, nomeDisciplina)
            }
        }
    }
    private fun cadDisciplina(nome:String, inst:String, email:String, curso:String, serie:String, nomeDisciplina:String){
        db = FirebaseFirestore.getInstance()
        val disciplina = hashMapOf(
            "cursos" to curso,
            "nome" to nomeDisciplina,
            "séries" to serie
        )
        db.collection("Adm").document(inst).collection("Disciplinas").document(nomeDisciplina)
            .set(disciplina)
            .addOnSuccessListener {
                Toast.makeText(this@TelaCadDisciplinas, "Disiciplina cadastrada com sucesso!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@TelaCadDisciplinas, TelaAdmProf::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("email", email)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
    }
}