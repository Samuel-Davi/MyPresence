package com.example.finalproject.view

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.model.BotaoConfirmado
import com.example.finalproject.databinding.ActivityTelaEscolhaBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException

class TelaEscolha : AppCompatActivity() {

    private lateinit var instituicoes:ArrayList<String>
    private lateinit var db:FirebaseFirestore
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaEscolhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btConfirm = BotaoConfirmado(false, false, false, 0)

        Log.d("MYLOG", "ta aq")

        binding.imgAdm.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtAdm){
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admon))
                btConfirm.valorBtAdm = true
                btConfirm.valorBtProf = false;
                btConfirm.valorBtAluno = false;
                btConfirm.valorConfirm = 3;
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
            }else{
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
                btConfirm.valorBtAdm = false
                btConfirm.valorConfirm = 0;
            }
        }
        binding.imgProf.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtProf){
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profon))
                btConfirm.valorBtProf = true
                btConfirm.valorBtAdm = false
                btConfirm.valorBtAluno = false
                btConfirm.valorConfirm = 2;
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
            }else{
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
                btConfirm.valorBtProf = false
                btConfirm.valorConfirm = 0;
            }
        }
        binding.imgALuno.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtAluno){
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunoon))
                btConfirm.valorBtAluno = true
                btConfirm.valorBtAdm = false
                btConfirm.valorBtProf = false
                btConfirm.valorConfirm = 1;
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
            }else{
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                btConfirm.valorBtAluno = false
                btConfirm.valorConfirm = 0;
            }
        }

        binding.btProx.setOnClickListener {
            if(btConfirm.valorConfirm == 1){
//                progressDialog = ProgressDialog(this)
                Toast.makeText(this, "ta aq", Toast.LENGTH_LONG).show()
//                progressDialog.setMessage("Carregando as instituições...")
//                progressDialog.setCancelable(false)
//                progressDialog.show()
                getInstituicoesAluno()
            }else if(btConfirm.valorConfirm == 2) {
                Toast.makeText(this, "ta aq", Toast.LENGTH_LONG).show()
//                progressDialog = ProgressDialog(this)
//                progressDialog.setMessage("Carregando as instituições...")
//                progressDialog.setCancelable(false)
//                progressDialog.show()
                getInstituicoesProf()
            }else if(btConfirm.valorConfirm == 3){
                startActivity(Intent(this, TelaLoginAdm::class.java))
            }else{
                Toast.makeText(this, "Por favor, selecione uma das opções para continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getInstituicoesProf() {
        Toast.makeText(this, "Prof", Toast.LENGTH_LONG).show()
        db = FirebaseFirestore.getInstance()
        try{
            db.collection("Adm").get()
            .addOnSuccessListener { documents->
                instituicoes = ArrayList()
                for (document in documents){
                    var nomeInst = document.get("instituicao").toString()
                    instituicoes.add(nomeInst)
                    Log.d("MYLOG", "pegou")
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    val intent = Intent(this, TelaLoginProf::class.java)
                    intent.putExtra("inst", instituicoes)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }catch (e:IOException){
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun getInstituicoesAluno() {
        Toast.makeText(this, "Aluno", Toast.LENGTH_LONG).show()
        db = FirebaseFirestore.getInstance()
        db.collection("Adm").get()
            .addOnSuccessListener { documents->
                instituicoes = ArrayList()
                for (document in documents){
                    var nomeInst = document.get("instituicao").toString()
                    instituicoes.add(nomeInst)
                    Log.d("MYLOG", "pegou")
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    val intent = Intent(this, TelaLoginAluno::class.java)
                    intent.putExtra("inst", instituicoes)
                    startActivity(intent)
                }
            }
    }
}
