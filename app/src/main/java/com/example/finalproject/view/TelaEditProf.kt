package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaEditProfBinding
import com.google.firebase.firestore.FirebaseFirestore

class TelaEditProf : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaEditProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val data = intent.extras
        val nome = data?.getString("nome")
        val email = data?.getString("email")
        val sob = data?.getString("sob")
        val disc = data?.getString("disciplina")

        binding.textemail.hint = email
        binding.textNome.hint = nome
        binding.textDisc.hint = disc
        binding.textSobrenome.hint = sob

        binding.btBack.setOnClickListener {
            val intent = Intent(this, TelaMainProfessor::class.java)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            intent.putExtra("disciplina", disc)
            intent.putExtra("sob", sob)
            startActivity(intent);
        }

        binding.btAlterar.setOnClickListener {
            if(binding.textemail.text.isNotEmpty()||binding.textSobrenome.text.isNotEmpty()||binding.textDisc.text.isNotEmpty()||binding.textNome.text.isNotEmpty()){
                updateProf(binding.textemail.text.toString(), binding.textNome.text.toString(), binding.textDisc.text.toString(), binding.textSobrenome.text.toString())
            }else{
                Toast.makeText(this, "Preencha os campos completos", Toast.LENGTH_LONG).show()
            }
            binding.textemail.text.clear()
            binding.textSobrenome.text.clear()
            binding.textDisc.text.clear()
            binding.textNome.text.clear()
        }
    }
    private fun updateProf(email:String, nome:String, disc:String, sob:String){
        db.collection("Professor").document(email)
            .update(mapOf(
                "nome" to nome,
                "sobrenome" to sob,
                "disciplina" to disc
            )).addOnSuccessListener {
                Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, TelaMainProfessor::class.java)
                intent.putExtra("email", email)
                intent.putExtra("nome", nome)
                intent.putExtra("disciplina", disc)
                intent.putExtra("sob", sob)
                startActivity(intent)
            }.addOnFailureListener{ e ->
                Log.e("TAG", e.toString())
                Toast.makeText(this, "Erro ao atualizar os dados", Toast.LENGTH_LONG).show()
            }
    }
}