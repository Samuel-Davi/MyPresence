package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaLoginAlunoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaLoginAluno : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var bd:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaLoginAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.txtEsqSenha.setOnClickListener {
            if(binding.textEmail.text.isEmpty()){
                Toast.makeText(this@TelaLoginAluno, "Preencha o Email", Toast.LENGTH_LONG).show()
            }else{
                auth.sendPasswordResetEmail(binding.textEmail.text.toString())
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this@TelaLoginAluno, "Email de redefinição de senha enviado", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@TelaLoginAluno, "Erro ao enviar o Email", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            binding.textEmail.text.clear()
            binding.textPassword.text.clear()
        }

        binding.botaoLogin.setOnClickListener {
            if(binding.textEmail.text.isNotEmpty() && binding.textPassword.text.isNotEmpty()){
                validaLogin(binding.textEmail.text.toString(), binding.textPassword.text.toString())
            }else{
                Toast.makeText(this@TelaLoginAluno, "Preencha os campos", Toast.LENGTH_LONG).show()
            }
            binding.textPassword.text.clear()
            binding.textEmail.text.clear()
        }
    }

    private fun validaLogin(email:String, senha:String){
        bd = FirebaseFirestore.getInstance()
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    Log.d("TAG", "deu certo")
                    bd.collection("Alunos")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents){
                                Log.d("TAG", "${document.data}")
                                val name = document.get("nome").toString()
                                val sob = document.get("sobrenome").toString()
                                val turma = document.get("turma").toString()
                                val ra = document.getLong("RA").toString()
                                val intent = Intent(this, TelaMainAluno::class.java)
                                intent.putExtra("nome", name);
                                intent.putExtra("turma", turma)
                                intent.putExtra("sob", sob)
                                intent.putExtra("email", email)
                                intent.putExtra("RA", ra)
                                startActivity(intent);
                                Log.d("TAG", "ta aq")
                                Toast.makeText(this, "Bem vindo(a) $name", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener { exception ->
                            Log.d("TAG", "get failed with ", exception)
                        }
                }else{
                    Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                }
            }
    }
}