package com.example.finalproject.view

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.databinding.ActivityTelaAdmProfBinding
import com.example.finalproject.fragment.AdmAccountFragment
import com.example.finalproject.fragment.AdmHomeFragment
import com.example.finalproject.model.Prof
import com.example.finalproject.R
import com.google.firebase.firestore.*
import org.opencv.android.OpenCVLoader


class TelaAdmProf : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgHome: ImageView
    private lateinit var imgAccount: ImageView

    private lateinit var homeFragment: AdmHomeFragment
    private lateinit var accountFragment: AdmAccountFragment

    private lateinit var db:FirebaseFirestore

    private lateinit var disciplinas:ArrayList<String>

//    private lateinit var bitmap:Bitmap



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

        val data = intent.extras
        val nome = data?.getString("nome")
        val senha = data?.getString("senha")
        val inst = data?.getString("inst")
        val email = data?.getString("email")
//        val bitmap = data?.getByteArray("bitmap")

        val fragmentTransaction =
            supportFragmentManager.beginTransaction().add(R.id.fragmentsAdmProf, homeFragment)
        fragmentTransaction.commit()


        with(binding){
            imgAdd.setOnClickListener {
                if(!valorClick){
//                    linearLayout2.isVisible = true
                    btCadProf.isVisible = true
                    btCadTurma.isVisible = true
                    btCadDisciplina.isVisible = true
                    valorClick = !valorClick
                }else{
//                    linearLayout2.isVisible = false
                    btCadProf.isVisible = false
                    btCadTurma.isVisible = false
                    btCadDisciplina.isVisible = false
                    valorClick = !valorClick
                }
            }
            btCadProf.setOnClickListener {
                getDisciplinas(inst.toString(), email.toString(), nome.toString())
            }
            btCadTurma.setOnClickListener {
                val intent = Intent(this@TelaAdmProf, TelaCadTurma::class.java)
                intent.putExtra("inst", inst)
                intent.putExtra("email", email)
                intent.putExtra("nome", nome)
                startActivity(intent)
            }
            btCadDisciplina.setOnClickListener{
                val intent = Intent(this@TelaAdmProf, TelaCadDisciplinas::class.java)
                intent.putExtra("inst", inst)
                intent.putExtra("email", email)
                intent.putExtra("nome", nome)
                startActivity(intent)
            }
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
        fragmentTransaction.replace(R.id.fragmentsAdmProf, fragment)
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
