package com.example.finalproject.view

import android.R
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.finalproject.databinding.ActivityTelaLoginAlunoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaLoginAluno : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var spinner: Spinner
    private var instituicoes: ArrayList<String>? = null
    lateinit var institutionSelection:String
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaLoginAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()
//        getInstituicao()
        auth = Firebase.auth

        val data = intent.extras
        instituicoes = data?.getStringArrayList("inst") as ArrayList<String>

        spinner = binding.spinnerInstTelaALuno

        spinner.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, instituicoes!!)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                institutionSelection = instituicoes!![position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        with(binding){
            txtEsqSenha.setOnClickListener {
                if(textEmailAluno.text.isEmpty()){
                    Toast.makeText(this@TelaLoginAluno, "Preencha o Email", Toast.LENGTH_LONG).show()
                }else{
                    auth.sendPasswordResetEmail(textEmailAluno.text.toString())
                        .addOnCompleteListener { task->
                            if(task.isSuccessful){
                                Toast.makeText(this@TelaLoginAluno, "Email de redefinição de senha enviado", Toast.LENGTH_LONG).show()
                            }else{
                                Toast.makeText(this@TelaLoginAluno, "Erro ao enviar o Email", Toast.LENGTH_LONG).show()
                            }
                        }
                }
                textEmailAluno.text.clear()
                textPassword.text.clear()
            }

            botaoLogin.setOnClickListener {
                if(textEmailAluno.text.isNotEmpty() && textPassword.text.isNotEmpty()){
                    progressDialog = ProgressDialog(this@TelaLoginAluno)
                    progressDialog.setMessage("Carregando...")
                    progressDialog.setCancelable(false)
                    progressDialog.show()
                    validaLogin(textEmailAluno.text.toString(), textPassword.text.toString(), institutionSelection)
                }else{
                    Toast.makeText(this@TelaLoginAluno, "Preencha os campos", Toast.LENGTH_LONG).show()
                }
                textPassword.text.clear()
                textEmailAluno.text.clear()
            }
        }
    }

//    private fun getInstituicao(){
//        db.collection("Adm").get()
//            .addOnSuccessListener { documents->
//                instituicoes = ArrayList()
//                for(document in documents) {
//                    var nomeInst = document.get("instituicao").toString()
//                    instituicoes.add(nomeInst)
//                }
//                Log.d("TAG", instituicoes[0])
//
//            }
//    }

    private fun validaLogin(email:String, senha:String, inst:String){
        db = FirebaseFirestore.getInstance()
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    Log.d("TAG", "deu certo")
                    db.collection("Adm").document(inst).collection("Alunos")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents){
                                Log.d("TAG", "${document.data}")
                                val name = document.get("nome").toString()
                                val sob = document.get("sobrenome").toString()
                                val turma = document.get("turma").toString()
                                val ra = document.get("ra").toString()
                                Log.d("TAG1", ra)
                                val intent = Intent(this, TelaMainAluno::class.java)
                                intent.putExtra("nome", name);
                                intent.putExtra("turma", turma)
                                intent.putExtra("sob", sob)
                                intent.putExtra("email", email)
                                intent.putExtra("ra", ra)
                                intent.putExtra("inst", inst)
                                if (progressDialog.isShowing) progressDialog.dismiss()
                                startActivity(intent);
                                Log.d("TAG", "ta aq")
                                Toast.makeText(this, "Bem vindo(a) $name", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener { exception ->
                            if (progressDialog.isShowing) progressDialog.dismiss()
                            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                            Log.d("TAG", "get failed with ", exception)
                        }
                }else{
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                }
            }
    }
}