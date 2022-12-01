package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaFaltasBinding
import com.google.firebase.firestore.FirebaseFirestore

class TelaFaltas : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore

    private lateinit var binding:ActivityTelaFaltasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaFaltasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()



        val data = intent.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")
        val bytes = data?.getByteArray("bitmap")

        db.collection("Adm").document(inst.toString()).collection("Alunos").document(ra.toString())
            .get()
            .addOnSuccessListener { data->
                binding.qtdFalta4Bim.text = data.getLong("totalFaltas").toString()
                binding.qtdFaltaTotal.text = data.getLong("totalFaltas").toString()
            }

        with(binding){
            btVoltarTelaFaltas.setOnClickListener {
                var intent = Intent(this@TelaFaltas, TelaMainAluno::class.java)
                intent.putExtra("nome", nome);
                intent.putExtra("turma", turma)
                intent.putExtra("sob", sob)
                intent.putExtra("email", email)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                intent.putExtra("bitmap", bytes)
                startActivity(intent)
            }
        }
    }
}