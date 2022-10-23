package com.example.finalproject.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.finalproject.databinding.ActivityTelaCadAdmBinding
import com.example.finalproject.util.ProgressDialogo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaCadAdm : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Carregando...")
                progressDialog.setCancelable(false)
                progressDialog.show()
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
                        .addOnSuccessListener {
                            val intent = Intent(this@TelaCadAdm, TelaAdmProf::class.java)
                            intent.putExtra("email",email)
                            intent.putExtra("inst", instituicao)
                            intent.putExtra("nome", nome)
                            startActivity(intent)
                            if (progressDialog.isShowing) progressDialog.dismiss()
                            Toast.makeText(this@TelaCadAdm, "Cadastro Realizado", Toast.LENGTH_LONG).show() }
                        .addOnFailureListener { e -> Log.e("TAG", "Error writing document", e);
                            if (progressDialog.isShowing) progressDialog.dismiss()
                            Toast.makeText(this@TelaCadAdm, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()}

                }else{
                    Toast.makeText(this@TelaCadAdm, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}
