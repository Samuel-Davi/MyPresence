package com.example.finalproject.fragment

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
import com.example.finalproject.model.Turma
import com.example.finalproject.recycler.DisciplinaAdapter
import com.example.finalproject.recycler.TurmaAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class ProfHomeFragment : Fragment() {

    private lateinit var recyclerViewTurmas: RecyclerView
    private lateinit var newArrayListTurmas:ArrayList<Turma>
    private lateinit var textNomeListTurma: ArrayList<String>
    private lateinit var textNomeTurma:Array<String>
    private lateinit var textSalaListTurma:ArrayList<String>
    private lateinit var textSalaTurma:Array<String>

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prof_home, container, false)



        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val disciplina = data?.getString("disciplina")
        val sob = data?.getString("sob")
        val email = data?.getString("email")

        val txtOla:TextView = view?.findViewById(R.id.txtOla)!!
        txtOla.text = "Olá, $nome"

        val txtDisciplina:TextView = view?.findViewById(R.id.txtDisciplina)!!
        txtDisciplina.text = "Disciplina: $disciplina"

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.extras
        val inst = data?.getString("inst")
        dataInitialize(inst.toString())
    }

    private fun dataInitialize(inst:String){
        textNomeListTurma = ArrayList()
        textSalaListTurma = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("Adm").document(inst).collection("Turmas")
            .get().addOnSuccessListener {documents->
                if(documents.isEmpty){
                    val txt = activity?.findViewById<TextView>(R.id.txtAdmProf)!!
                    val img = activity?.findViewById<ImageView>(R.id.imgAdmProf)!!
                }else {
                    for (document in documents) {
                        var nome = document.get("nome").toString()
                        var sala = document.get("sala").toString()
                        Log.d("MYLOG", nome)
                        textNomeListTurma.add(nome)
                        textSalaListTurma.add(sala)
//                        Log.d("TAG12", textNomeListDisc[1] + textProfNomeList[1])

                        textNomeTurma = textNomeListTurma.toTypedArray()
                        textSalaTurma = textSalaListTurma.toTypedArray()
//                        Log.d("TAG", textNomeDisc[0] + " " + textNomeDisc[1])

                        newArrayListTurmas = arrayListOf<Turma>()
                        getUserData()
                    }
                }
            }.addOnFailureListener { exception->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun getUserData(){
        newArrayListTurmas = ArrayList()
        for(i in textNomeTurma.indices){
            val turma = Turma(textNomeTurma[i], textSalaTurma[i])
            newArrayListTurmas.add(turma)
            Log.d("MYLOG", newArrayListTurmas[i].toString())
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerViewTurmas = view?.findViewById(R.id.recyclerViewProfTurmas)!!
        recyclerViewTurmas.layoutManager = layoutManager
        recyclerViewTurmas.setHasFixedSize(true)
        recyclerViewTurmas.adapter = TurmaAdapter(newArrayListTurmas)
    }

}