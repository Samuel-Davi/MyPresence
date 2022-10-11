package com.example.finalproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.finalproject.R
import com.example.finalproject.view.TelaEditAdm
import com.example.finalproject.view.TelaEditProf
import com.example.finalproject.view.TelaEscolha
import com.example.finalproject.view.TelaTchau
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfAccountFragment : Fragment() {

    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prof_account, container, false)

        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val disciplina = data?.getString("disciplina")
        val sob = data?.getString("sob")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

        val btSair:TextView = view?.findViewById(R.id.txtSair)!!
        btSair.setOnClickListener {
            startActivity(Intent(activity, TelaEscolha::class.java))
        }

        val btEdit: TextView = view?.findViewById(R.id.txtEditProf)!!
        btEdit.setOnClickListener {
            val intent = Intent(activity, TelaEditProf::class.java)
            intent.putExtra("email", email)
            intent.putExtra("disciplina", disciplina)
            intent.putExtra("nome", nome)
            intent.putExtra("sob", sob)
            startActivity(intent)
        }

        val linearCaixaDel: LinearLayout = view?.findViewById(R.id.linearCaixaDel)!!
        val txtDel: TextView = view?.findViewById(R.id.txtDel)!!
        txtDel.setOnClickListener {
            linearCaixaDel.isVisible = true
            txtDel.isVisible = false
        }

        val txtVoltar: TextView = view?.findViewById(R.id.txtVoltar)!!
        txtVoltar.setOnClickListener {
            linearCaixaDel.isVisible = false
            txtDel.isVisible = true
        }

        val txtExcluir: TextView = view?.findViewById(R.id.txtExcluirProf)!!
        txtExcluir.setOnClickListener {
            deleteProf(email.toString(), inst.toString())
            linearCaixaDel.isVisible = false
            txtDel.isVisible = true;
        }

        return view
    }

    private fun deleteProf(email:String, inst:String){
        db = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        user!!.delete()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    db.collection("Adm").document(inst).collection("Professores").document(email)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(activity, "Conta deletada com Sucesso", Toast.LENGTH_LONG).show();
                            startActivity(Intent(activity, TelaTchau::class.java))
                        }
                        .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
                }
            }
    }
}