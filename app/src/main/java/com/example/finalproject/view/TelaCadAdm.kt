package com.example.finalproject.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityTelaCadAdmBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaCadAdm : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth;
        db = FirebaseFirestore.getInstance()
        val binding = ActivityTelaCadAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtJaTemConta.setOnClickListener {
            startActivity(Intent(this, TelaLoginAdm::class.java));
        }
        binding.btConfirma.setOnClickListener {
            if(binding.textEmail.text.isEmpty() || binding.textNickname.text.isEmpty() || binding.textSenha.text.isEmpty() || binding.textInstituicao.text.isEmpty()){
                Toast.makeText(this,"Preencha os campos corretamente", Toast.LENGTH_LONG).show();
            }else{
                cadAdm(binding.textNickname.text.toString(), binding.textEmail.text.toString(), binding.textSenha.text.toString(), binding.textInstituicao.text.toString().uppercase())
                binding.textEmail.text.clear()
                binding.textNickname.text.clear()
                binding.textSenha.text.clear()
                binding.textInstituicao.text.clear()
            }
        }
    }
    private fun cadAdm(nome:String, email:String, senha:String, instituicao:String){
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task->
                if(task.isSuccessful){
                    val adm = hashMapOf(
                        "nome" to nome,
                        "email" to email,
                        "instituicao" to instituicao
                    )
                    db.collection("Adm").document(instituicao)
                        .set(adm)
                        .addOnSuccessListener { startActivity(Intent(this@TelaCadAdm, TelaLoginAdm::class.java))
                            Toast.makeText(this@TelaCadAdm, "Cadastro Realizado", Toast.LENGTH_LONG).show() }
                        .addOnFailureListener { e -> Log.e("TAG", "Error writing document", e);
                            Toast.makeText(this@TelaCadAdm, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()}

                }else{
                    Toast.makeText(this@TelaCadAdm, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()
                }
            }
    }
}
