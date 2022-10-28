package com.example.finalproject.view

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaMainAlunoBinding
import com.example.finalproject.fragment.*
import com.example.finalproject.model.Disciplina
import com.example.finalproject.model.Prof
import com.example.finalproject.recycler.DisciplinaAdapter
import com.example.finalproject.recycler.ProfAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TelaMainAluno : AppCompatActivity() {

    private lateinit var recyclerViewDisciplinas: RecyclerView
    private lateinit var newArrayListDisc:ArrayList<Disciplina>
    private lateinit var textNomeListDisc: ArrayList<String>
    private lateinit var textNomeDisc:Array<String>
    private lateinit var textProfNomeList:ArrayList<String>
    private lateinit var textProfNome:Array<String>
    private lateinit var cursosList:ArrayList<String>
    private lateinit var cursos:Array<String>
    private lateinit var seriesList:ArrayList<String>
    private lateinit var series:Array<String>

    private lateinit var db:FirebaseFirestore

    private var storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaMainAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

//        dataInitialize(inst.toString())

        val layoutManager = LinearLayoutManager(this@TelaMainAluno)
        recyclerViewDisciplinas = findViewById(R.id.recyclerViewDisciplinas)
        recyclerViewDisciplinas.layoutManager = layoutManager
        recyclerViewDisciplinas.setHasFixedSize(true)

        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/alunos/$ra/$ra.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                binding.imgAlunoMain.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }

        with(binding){
            txtOla.text = "Olá, $nome"
            txtDisciplina.text = "Turma: $turma"

            imgAccount.setOnClickListener {
                val intent = Intent(this@TelaMainAluno, TelaOpcAluno::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("sob", sob)
                intent.putExtra("turma", turma)
                intent.putExtra("email", email)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                startActivity(intent);
            }
            imgIniciaRF.setOnClickListener {
                val intent = Intent(this@TelaMainAluno, TelaCamera::class.java)
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

    private fun dataInitialize(inst:String){
        textNomeListDisc = ArrayList()
        textProfNomeList = ArrayList()
        cursosList = ArrayList()
        seriesList = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("Adm").document(inst).collection("Disciplinas")
            .get().addOnSuccessListener {documents->
                if(documents.isEmpty){
                    val txt = findViewById<TextView>(R.id.txtAdmProf)
                    val img = findViewById<ImageView>(R.id.imgAdmProf)
                }else {
                    for (document in documents) {
                        var nome = document.get("nome").toString()
                        var nomeProf = document.get("nomeProf").toString()
                        var cursosBanco = document.get("cursos").toString()
                        var seriesBanco = document.get("séries").toString()
                        Log.d("TAG", nome)
                        textNomeListDisc.add(nome)
                        textProfNomeList.add(nomeProf)
                        cursosList.add(cursosBanco)
                        seriesList.add(seriesBanco)
//                        Log.d("TAG12", textNomeListDisc[1] + textProfNomeList[1])

                        textNomeDisc = textNomeListDisc.toTypedArray()
                        textProfNome = textProfNomeList.toTypedArray()
                        cursos = cursosList.toTypedArray()
                        series = seriesList.toTypedArray()
//                        Log.d("TAG", textNomeDisc[0] + " " + textNomeDisc[1])

                        newArrayListDisc = arrayListOf<Disciplina>()
                        getUserData()
                    }
                }
            }.addOnFailureListener { exception->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun getUserData(){
        for(i in textNomeDisc.indices){
            val disciplina = Disciplina(cursos[i], textNomeDisc[i], series[i], textProfNome[i])
            newArrayListDisc.add(disciplina)
            Log.d("TelaMainAlunoTeste", newArrayListDisc[i].toString())
        }

        recyclerViewDisciplinas.adapter = DisciplinaAdapter(newArrayListDisc)
    }
}