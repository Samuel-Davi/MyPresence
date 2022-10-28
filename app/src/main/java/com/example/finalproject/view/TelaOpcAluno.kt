package com.example.finalproject.view

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaOpcAlunoBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TelaOpcAluno : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    private var storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaOpcAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()

        val data = intent.extras
        val email = data?.getString("email")
        val turma = data?.getString("turma")
        val ra = data?.getString("ra")
        Log.d("TAG!", ra.toString())
        val nome = data?.getString("nome")
        val sob = data?.getString("sob")
        val inst = data?.getString("inst")

        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/alunos/$ra/$ra.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                binding.imgEditTelaOpc.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }

        with(binding){
            imgHome.setOnClickListener {
                val intent = Intent(this@TelaOpcAluno, TelaMainAluno::class.java)
                intent.putExtra("email", email)
                intent.putExtra("turma", turma)
                intent.putExtra("nome", nome)
                intent.putExtra("sob", sob)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }

            txtSair.setOnClickListener {
                val intent = Intent(this@TelaOpcAluno, TelaEscolha::class.java)
                startActivity(intent)
            }
            imgEditTelaOpcAluno.setOnClickListener {
                val intent = Intent(this@TelaOpcAluno, TelaEditFotoAluno::class.java)
                intent.putExtra("email", email)
                intent.putExtra("turma", turma)
                intent.putExtra("nome", nome)
                intent.putExtra("sob", sob)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }

            imgcamera.setOnClickListener{
                val intent = Intent(this@TelaOpcAluno, TelaCamera::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("sob", sob)
                intent.putExtra("turma", turma)
                intent.putExtra("email", email)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }

        }
    }
}