package com.example.finalproject.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.ActivityTelaAdmProfBinding
import com.example.finalproject.fragment.AdmAccountFragment
import com.example.finalproject.fragment.AdmHomeFragment
import com.example.finalproject.model.Prof
import com.example.finalproject.R
import com.google.firebase.firestore.*


class TelaAdmProf : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgHome: ImageView
    private lateinit var imgAccount: ImageView

    private lateinit var homeFragment: AdmHomeFragment
    private lateinit var accountFragment: AdmAccountFragment

    private lateinit var db:FirebaseFirestore

    private lateinit var disciplinas:ArrayList<String>


    private lateinit var profs:ArrayList<Prof>
    private lateinit var recyclerView: RecyclerView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaAdmProfBinding.inflate(layoutInflater)
        var valorClick:Boolean = false;
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()


        imgHome = findViewById(R.id.imgHome)
        imgHome.setOnClickListener(this)

        imgAccount = findViewById(R.id.imgAccount)
        imgAccount.setOnClickListener(this)

        homeFragment = AdmHomeFragment()
        accountFragment = AdmAccountFragment()


        val fragmentTransaction =
            supportFragmentManager.beginTransaction().add(R.id.fragments, homeFragment)
        fragmentTransaction.commit()


        val data = intent.extras
        val nome = data?.getString("nome")
        val senha = data?.getString("senha")
        val inst = data?.getString("inst")
        val email = data?.getString("email")


        binding.imgAdd.setOnClickListener {
            if(!valorClick){
                binding.btCadProf.isVisible = true
                binding.btCadTurma.isVisible = true
                valorClick = !valorClick
            }else{
                binding.btCadProf.isVisible = false
                binding.btCadTurma.isVisible = false
                valorClick = !valorClick
            }
        }
        binding.btCadProf.setOnClickListener {
            getDisciplinas(inst.toString(), email.toString(), nome.toString())
        }
        binding.btCadTurma.setOnClickListener {
            val intent = Intent(this, TelaCadTurma::class.java)
            intent.putExtra("inst", inst)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            startActivity(intent)
        }
    }

    private fun getDisciplinas(inst: String, email:String, nome:String){
        db = FirebaseFirestore.getInstance()
        val list = db.collection("Adm").document(inst).collection("Disciplinas")
        list.get().addOnSuccessListener {documents ->
            disciplinas = ArrayList()
            for(document in documents){
                var nomeDisc = document.get("nome").toString()
                disciplinas.add(nomeDisc)
                Log.d("TAG", disciplinas.toString())
            }
            val intent = Intent(this, TelaCadProf::class.java)
            intent.putExtra("inst", inst)
            intent.putExtra("email", email)
            intent.putExtra("nome", nome)
            intent.putExtra("disciplinas", disciplinas)
            startActivity(intent)
        }.addOnFailureListener {
            Log.w("TAG", "Error getting documents: ", it)
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragments, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View) {
        var imgHome:ImageView = findViewById(R.id.imgHome)
        var imgAccount:ImageView = findViewById(R.id.imgAccount)
        when(v.id){
            R.id.imgAccount ->{
                replaceFragment(accountFragment)
                imgHome.setImageDrawable(getDrawable(R.drawable.imghomedes))
                imgAccount.setImageDrawable(getDrawable(R.drawable.imguserbox))
            }
            R.id.imgHome ->{
                replaceFragment(homeFragment)
                imgHome.setImageDrawable(getDrawable(R.drawable.imghome))
                imgAccount.setImageDrawable(getDrawable(R.drawable.imgaccountdes))
            }
        }
    }
}
