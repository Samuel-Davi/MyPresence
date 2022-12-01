package com.example.finalproject.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.finalproject.R
import com.example.finalproject.view.TelaEditFotoAluno
import com.example.finalproject.view.TelaEscolha
import com.example.finalproject.view.TelaFaltas
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_account_aluno.*
import java.io.File

class FragmentAccountAluno : Fragment() {

    private lateinit var db: FirebaseFirestore
    private var storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account_aluno, container, false)

        db = FirebaseFirestore.getInstance()

        val data = activity?.intent?.extras
        val email = data?.getString("email")
        val turma = data?.getString("turma")
        val ra = data?.getString("ra")
        val nome = data?.getString("nome")
        val sob = data?.getString("sob")
        val inst = data?.getString("inst")
        val bytes = data?.getByteArray("bitmap")

        try {
            val bytes = data?.getByteArray("bitmap")
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size)
            val imgEditTelaOpc:ImageView = view?.findViewById(R.id.imgEditTelaOpc)!!
            imgEditTelaOpc.setImageBitmap(bitmap)
        }catch (e:Exception){
            Log.e("MYLOG", e.toString())
        }


        val txtSair:TextView = view?.findViewById(R.id.txtSairAluno)!!

        txtSair.setOnClickListener {
            val intent = Intent(activity, TelaEscolha::class.java)
            startActivity(intent)
        }

        val imgEditTelaOpcAluno:ImageView = view?.findViewById(R.id.imgEditTelaOpcAluno)!!
        imgEditTelaOpcAluno.setOnClickListener {
            val intent = Intent(activity, TelaEditFotoAluno::class.java)
            intent.putExtra("email", email)
            intent.putExtra("turma", turma)
            intent.putExtra("nome", nome)
            intent.putExtra("sob", sob)
            intent.putExtra("ra", ra)
            intent.putExtra("inst", inst)

            startActivity(intent)
        }

        val linearViewFaltas:LinearLayout = view?.findViewById(R.id.linearViewFaltas)!!
        linearViewFaltas.setOnClickListener {
            var intent = Intent(activity, TelaFaltas::class.java)
            intent.putExtra("nome", nome);
            intent.putExtra("turma", turma)
            intent.putExtra("sob", sob)
            intent.putExtra("email", email)
            intent.putExtra("ra", ra)
            intent.putExtra("inst", inst)
            intent.putExtra("bitmap", bytes)
            startActivity(intent)
        }

        return view;
    }



}