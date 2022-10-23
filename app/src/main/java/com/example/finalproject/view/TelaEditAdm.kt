package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.WindowCompat
import com.example.finalproject.databinding.ActivityTelaEditAdmBinding
import com.example.finalproject.model.UserModel
import com.example.finalproject.network.ApiClient
import com.example.finalproject.network.InterfaceApi
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TelaEditAdm : AppCompatActivity() {
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        val binding = ActivityTelaEditAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        val data = intent.extras
        val nome = data?.getString("nome")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

        binding.textemail.hint = email
        binding.textNome.hint = nome

        binding.btBack.setOnClickListener {
            val intent = Intent(this, TelaAdmProf::class.java)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            intent.putExtra("inst", inst)
            startActivity(intent);
        }

        binding.btAlterar.setOnClickListener {
            updateAdm(binding.textemail.text.toString(), binding.textNome.text.toString(), inst.toString())
            binding.textemail.text.clear()
            binding.textNome.text.clear()
        }
    }
    private fun updateAdm(email:String, nome:String, inst:String){

        db.collection("Adm").document(inst)
            .update(mapOf(
                "email" to email,
                "nome" to nome
            )).addOnSuccessListener {
                Toast.makeText(this, "Atualizado com sucesso!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, TelaAdmProf::class.java)
                intent.putExtra("email", email)
                intent.putExtra("nome", nome)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Erro ao atualizar os dados", Toast.LENGTH_LONG).show()
            }
    }
}