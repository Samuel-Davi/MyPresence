package com.example.finalproject.view

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
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaLoginProfBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TelaLoginProf : AppCompatActivity() {
    private lateinit var instituicoes:ArrayList<String>
    private lateinit var spinner: Spinner
    private lateinit var db:FirebaseFirestore
    private lateinit var auth:FirebaseAuth
    lateinit var instituicaoSelecionada:String
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
//        getInstituicao()
        auth = FirebaseAuth.getInstance()
        val binding = ActivityTelaLoginProfBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val data = intent.extras
        instituicoes = data?.getStringArrayList("inst") as ArrayList<String>

        var estados = arrayOf("amazonas", "sao paulo", "santa catarina")

        spinner = findViewById(R.id.spinnerInst)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, instituicoes)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                instituicaoSelecionada = instituicoes[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.txtEsqSenha.setOnClickListener {
            if(binding.textEmail.text.isEmpty()){
                Toast.makeText(this@TelaLoginProf, "Preencha o Email", Toast.LENGTH_LONG).show()
            }else{
                auth.sendPasswordResetEmail(binding.textEmail.text.toString())
                    .addOnCompleteListener { task->
                        if(task.isSuccessful){
                            Toast.makeText(this@TelaLoginProf, "Email de redefinição de senha enviado", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(this@TelaLoginProf, "Erro ao enviar o Email", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            binding.textEmail.text.clear()
            binding.textPassword.text.clear()
        }

        binding.botaoLogin.setOnClickListener {
            if(binding.textEmail.text.isNotEmpty() && binding.textPassword.text.isNotEmpty()){
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Carregando...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                validaLogin(binding.textEmail.text.toString(), binding.textPassword.text.toString())
            }else{
                Toast.makeText(this@TelaLoginProf, "Preencha os campos", Toast.LENGTH_LONG).show()
            }
            binding.textPassword.text.clear()
            binding.textEmail.text.clear()
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

    private fun validaLogin(email:String, senha:String){
        auth.signInWithEmailAndPassword(email,senha)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    db.collection("Adm").document(instituicaoSelecionada).collection("Professores")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents){
                                Log.d("TAG", "${document.data}")
                                val name = document.get("nome").toString()
                                val sob = document.get("sobrenome").toString()
                                val disciplina = document.get("disciplina").toString()
                                val intent = Intent(this, TelaMainProfessor::class.java)
                                intent.putExtra("nome", name);
                                intent.putExtra("disciplina", disciplina)
                                intent.putExtra("sob", sob)
                                intent.putExtra("email", email)
                                intent.putExtra("inst", instituicaoSelecionada)
                                startActivity(intent);
                                if(progressDialog.isShowing) progressDialog.dismiss()
                                Toast.makeText(this, "Bem vindo(a) $name", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener { exception ->
                            Log.d("TAG", "get failed with ", exception)
                        }
                }else{
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_LONG).show()
                }
            }
    }
}