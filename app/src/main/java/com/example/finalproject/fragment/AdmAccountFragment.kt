package com.example.finalproject.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.finalproject.R
import com.example.finalproject.view.TelaEditAdm
import com.example.finalproject.view.TelaEditFotoAdm
import com.example.finalproject.view.TelaEscolha
import com.example.finalproject.view.TelaTchau
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.net.URI


class AdmAccountFragment : Fragment() {

    private lateinit var db:FirebaseFirestore
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_adm_account, container, false)

        val data = activity?.intent?.extras
        val email = data?.getString("email")
        val senha = data?.getString("senha")
        val inst = data?.getString("inst")
        val nome = data?.getString("nome")

        val imageAdmAccountFragment:ImageView = view?.findViewById(R.id.imgEditAdm)!!

        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/admImage.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                imageAdmAccountFragment.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }

        val btSair:TextView = view?.findViewById(R.id.txtSair)!!
        btSair.setOnClickListener {
            startActivity(Intent(activity, TelaEscolha::class.java))
        }

        val btEdit:TextView = view?.findViewById(R.id.txtEdit)!!
        btEdit.setOnClickListener {
            val intent = Intent(activity, TelaEditAdm::class.java)
            intent.putExtra("email", email)
            intent.putExtra("senha", senha)
            intent.putExtra("nome", nome)
            intent.putExtra("inst", inst)
            startActivity(intent)
        }

        val linearCaixaDel:LinearLayout = view?.findViewById(R.id.linearCaixaDel)!!
        val txtDel:TextView = view?.findViewById(R.id.txtDel)!!
        txtDel.setOnClickListener {
            linearCaixaDel.isVisible = true
            txtDel.isVisible = false
        }

        val txtVoltar:TextView = view?.findViewById(R.id.txtVoltar)!!
        txtVoltar.setOnClickListener {
            linearCaixaDel.isVisible = false
            txtDel.isVisible = true
        }

        val txtExcluir:TextView = view?.findViewById(R.id.txtExcluir)!!
        txtExcluir.setOnClickListener {
            deleteAdm(inst.toString())
            linearCaixaDel.isVisible = false
            txtDel.isVisible = true;
        }

        val btEditFoto:ImageView = view?.findViewById(R.id.btEditFotoAdm)!!
        btEditFoto.setOnClickListener {
            val intent = Intent(activity, TelaEditFotoAdm::class.java)
            intent.putExtra("email", email)
            intent.putExtra("senha", senha)
            intent.putExtra("nome", nome)
            intent.putExtra("inst", inst)
            startActivity(intent)
        }

//        val layoutEditFoto:ConstraintLayout = view?.findViewById(R.id.layoutEditFoto)!!
//        layoutEditFoto.setOnClickListener {
//            val intent = Intent(activity, TelaEditFotoAdm::class.java)
//            intent.putExtra("email", email)
//            intent.putExtra("senha", senha)
//            intent.putExtra("nome", nome)
//            intent.putExtra("inst", inst)
//            startActivity(intent)
//        }

//        val imgEditAccountFragment:ImageView = view?.findViewById(R.id.imgEdit)!!
//        imgEditAccountFragment.setOnClickListener {
//            val intent = Intent(activity, TelaEditFotoAdm::class.java)
//            intent.putExtra("email", email)
//            intent.putExtra("senha", senha)
//            intent.putExtra("nome", nome)
//            intent.putExtra("inst", inst)
//            startActivity(intent)
//        }

        return view;
    }


    private fun deleteAdm(inst:String){
        val user = Firebase.auth.currentUser
        user!!.delete()
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    db = FirebaseFirestore.getInstance()
                    db.collection("Adm").document(inst)
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