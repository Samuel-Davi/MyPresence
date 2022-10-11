package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaCadProfBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class TelaCadProf : AppCompatActivity() {

    private lateinit var texto:TextView
    private lateinit var spinner:Spinner
    private lateinit var disciplinas:ArrayList<String>
    private lateinit var auth:FirebaseAuth
    private lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        val binding = ActivityTelaCadProfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var estados = arrayOf("amazonas", "rio", "sao paulo")
        estados.forEach { Log.d("TAG1", it) }

        var disciplinaSelecionada = ""

        val data = intent.extras
        val inst = data?.getString("inst")
        val nomeAdm = data?.getString("nome")
        val emailAdm = data?.getString("email")
        disciplinas = data?.getStringArrayList("disciplinas")!!


        spinner = findViewById(R.id.spinner)
        texto = findViewById(R.id.textoSpinner)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, disciplinas)

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                disciplinaSelecionada = disciplinas[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.btCad.setOnClickListener {
            if(binding.edtNome.text.isEmpty() || binding.edtSenha.text.isEmpty()
                || binding.edtEmail.text.isEmpty()) {
                Toast.makeText(this, "Preencha os campos corretamente", Toast.LENGTH_LONG).show();
            }else if(binding.edtSenha.length() < 6){
                Toast.makeText(this, "Senha deve ter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
            }else{
                cadProf( binding.edtNome.text.toString(), binding.edtSobrenome.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtSenha.text.toString(),
                    inst.toString(), nomeAdm.toString(), emailAdm.toString(), disciplinaSelecionada);
                    binding.edtNome.text.clear()
                    binding.edtSobrenome.text.clear()
                    binding.edtEmail.text.clear()
                    binding.edtSenha.text.clear()
            }
        }
        binding.btBack.setOnClickListener {
            val intent = Intent(this, TelaAdmProf::class.java)
            intent.putExtra("inst", inst)
            intent.putExtra("email", emailAdm)
            intent.putExtra("nome", nomeAdm)
            startActivity(intent)
        }
    }
    private fun cadProf(nome:String, sobrenome:String, email:String, senha:String, inst:String, nomeAdm:String, emailAdm:String, disciplina:String){

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val intent = Intent(this, TelaAdmProf::class.java)
                    intent.putExtra("inst", inst)
                    intent.putExtra("email", emailAdm)
                    intent.putExtra("nome", nomeAdm)
                    startActivity(intent)
                    Toast.makeText(this@TelaCadProf, "Cadastro Realizado", Toast.LENGTH_LONG).show()
                    saveFirestore(nome,sobrenome, email, inst, disciplina);
                }else{
                    Log.d("email", email);
                    Log.d("erro", task.exception.toString()); Toast.makeText(this@TelaCadProf, "Erro no Cadastro, tente de novo", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun saveFirestore(nome:String, sobrenome: String, email:String, inst:String, disciplina: String){
        db = FirebaseFirestore.getInstance()
        Log.d("TAG", inst);
        val prof = hashMapOf(
            "nome" to nome,
            "sobrenome" to sobrenome,
            "email" to email,
            "disciplina" to disciplina,
            "instituicao" to inst
        )
        db.collection("Adm").document(inst).collection("Professores").document(email)
            .set(prof)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }
    }
}
