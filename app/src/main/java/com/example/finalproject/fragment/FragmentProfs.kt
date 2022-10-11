package com.example.finalproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.model.Prof
import com.example.finalproject.recycler.ProfAdapter
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var adapter:ProfAdapter
private lateinit var recyclerView: RecyclerView
private lateinit var newArrayList:ArrayList<Prof>
lateinit var textNomeList: ArrayList<String>
lateinit var textNome:Array<String>
lateinit var textSobrenomeList:ArrayList<String>
lateinit var textSobrenome:Array<String>
lateinit var textDisciplinaList:ArrayList<String>
lateinit var textDisciplina:Array<String>

private lateinit var db: FirebaseFirestore

class FragmentProfs : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = activity?.intent?.extras
        val inst = data?.getString("inst")
        dataInitialize(inst.toString())
    }

    private fun dataInitialize(inst:String){
        textNomeList = ArrayList()
        textSobrenomeList = ArrayList()
        textDisciplinaList = ArrayList()
        db = FirebaseFirestore.getInstance()
        db.collection("Adm").document(inst).collection("Professores")
            .get().addOnSuccessListener {documents->
                if(documents.isEmpty){
                    val txt = view?.findViewById<TextView>(R.id.txtAdmProf)
                    txt?.isVisible = true
                    val img = view?.findViewById<ImageView>(R.id.imgAdmProf)
                    img?.isVisible = true
                }else{
                    for (document in documents){
                        Log.d("TAG", "${document.id} => ${document.data}")
                        var nome = document.get("nome").toString()
                        var sobrenome = document.get("sobrenome").toString()
                        var disciplina = document.get("disciplina").toString()
                        textNomeList.add(nome)
                        textSobrenomeList.add(sobrenome)
                        textDisciplinaList.add(disciplina)

                        textNome = textNomeList.toTypedArray()
                        textSobrenome = textSobrenomeList.toTypedArray()
                        textDisciplina = textDisciplinaList.toTypedArray()
                        getUserData()
                    }
                }
            }.addOnFailureListener { exception->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun getUserData(){
        newArrayList = arrayListOf<Prof>()
            for(i in textNome.indices){
                val prof = Prof(textNome[i], textSobrenome[i], textDisciplina[i])
                newArrayList.add(prof)
            }
            val layoutManager = LinearLayoutManager(context)
            recyclerView = view?.findViewById(R.id.recyclerViewProfs)!!
            recyclerView.layoutManager = layoutManager
            recyclerView.setHasFixedSize(true)
            adapter = ProfAdapter(newArrayList)
            recyclerView.adapter = adapter
        }
}