package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.finalproject.databinding.ActivityTelaOpcAlunoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaOpcAluno : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOpcAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()

        val data = intent.extras
        val email = data?.getString("email")
        val turma = data?.getString("turma")
        val ra = data?.getString("RA")
        val nome = data?.getString("nome")
        val sob = data?.getString("sob")

        binding.imgHome.setOnClickListener {
            val intent = Intent(this, TelaMainAluno::class.java)
            intent.putExtra("email", email)
            intent.putExtra("turma", turma)
            intent.putExtra("nome", nome)
            intent.putExtra("sob", sob)
            intent.putExtra("RA", ra)
            startActivity(intent)
        }

        binding.txtSair.setOnClickListener {
            val intent = Intent(this, TelaEscolha::class.java)
            startActivity(intent)
        }

//        binding.txtEdit.setOnClickListener {
//            val intent = Intent(this, TelaEditAdm::class.java)
//            intent.putExtra("email", email)
//            intent.putExtra("turma", turma)
//            intent.putExtra("nome", nome)
//            intent.putExtra("sob", sob)
//            startActivity(intent)
//        }
        binding.txtDel.setOnClickListener {
            binding.linearCaixaDel.isVisible = true;
            binding.txtDel.isVisible = false
        }
        binding.txtVoltar.setOnClickListener {
            binding.linearCaixaDel.isVisible = false
            binding.txtDel.isVisible = true
        }
        binding.txtExcluir.setOnClickListener {
            deleteAluno(email.toString())
            binding.linearCaixaDel.isVisible = false
            binding.txtDel.isVisible = true;
        }
    }
    private fun deleteAluno(email:String){
        val user = Firebase.auth.currentUser
        user!!.delete()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    db.collection("Adm").document(email)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Conta deletada com Sucesso", Toast.LENGTH_LONG).show();
                            startActivity(Intent(this, TelaTchau::class.java))
                        }
                        .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                }
            }
    }
}