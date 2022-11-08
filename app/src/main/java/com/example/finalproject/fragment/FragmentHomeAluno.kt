package com.example.finalproject.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.model.Disciplina
import com.example.finalproject.recycler.DisciplinaAdapter
import com.example.finalproject.recycler.ProfAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_home_aluno.*
import java.io.File

class FragmentHomeAluno : Fragment() {

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

    private lateinit var db: FirebaseFirestore

    private var storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_aluno, container, false)

        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")


//        dataInitialize(inst.toString())

//        val layoutManager = LinearLayoutManager(activity)
//        recyclerViewDisciplinas = view.findViewById(R.id.recyclerViewDisciplinas)
//        recyclerViewDisciplinas.layoutManager = layoutManager
//        recyclerViewDisciplinas.setHasFixedSize(true)

        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/alunos/$ra/$ra.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                imgAlunoMain.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }

        val txtOlaAluno:TextView = view.findViewById(R.id.txtOlaAluno)
        txtOlaAluno.text = "Olá, $nome"

        val txtTurmaAluno:TextView = view.findViewById(R.id.txtTurmaAluno)
        txtTurmaAluno.text = "Turma: $turma"

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.extras
        val inst = data?.getString("inst")
        dataInitialize(inst.toString())
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
                    val txt = activity?.findViewById<TextView>(R.id.txtAdmProf)!!
                    val img = activity?.findViewById<ImageView>(R.id.imgAdmProf)!!
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
        newArrayListDisc = ArrayList()
        for(i in textNomeDisc.indices){
            val disciplina = Disciplina(cursos[i], textNomeDisc[i], series[i], textProfNome[i])
            newArrayListDisc.add(disciplina)
            Log.d("TelaMainAlunoTeste", newArrayListDisc[i].toString())
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerViewDisciplinas = view?.findViewById(R.id.recyclerViewDisciplinas)!!
        recyclerViewDisciplinas.layoutManager = layoutManager
        recyclerViewDisciplinas.setHasFixedSize(true)
        recyclerViewDisciplinas.adapter = DisciplinaAdapter(newArrayListDisc)
    }

}