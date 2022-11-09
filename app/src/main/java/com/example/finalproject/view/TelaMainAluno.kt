package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaMainAlunoBinding
import com.example.finalproject.fragment.*
import com.example.finalproject.model.Disciplina
import com.example.finalproject.opencv.OpenCVTeste
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class TelaMainAluno : AppCompatActivity() , View.OnClickListener{

    private lateinit var recyclerViewDisciplinas: RecyclerView
    private lateinit var newArrayListDisc:ArrayList<Disciplina>
    private lateinit var textNomeListDisc: ArrayList<String>
    private lateinit var textNomeDisc:Array<String>
    private lateinit var textProfNomeList:ArrayList<String>
    private lateinit var textProfNome:Array<String>
    private lateinit var cursosList:ArrayList<String>
    private lateinit var cursos:Array<String>
    private lateinit var seriesList:ArrayList<String>
    private lateinit var series:Array<String>

    private lateinit var db:FirebaseFirestore

    private var storageRef = FirebaseStorage.getInstance().reference

    private lateinit var imgHome:ImageView
    private lateinit var imgAccount:ImageView
    private lateinit var imgCamera:ImageView

    private lateinit var homeFragment:FragmentHomeAluno
    private lateinit var accountFragment: FragmentAccountAluno

    private lateinit var binding:ActivityTelaMainAlunoBinding

    private lateinit var navHostFragment:NavHostFragment
    private lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaMainAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

//        dataInitialize(inst.toString())


        imgCamera = findViewById(R.id.imgIniciaRF)

        imgHome = findViewById(R.id.imgHomeAluno)
        imgHome.setOnClickListener(this)

        imgAccount = findViewById(R.id.imgAccountAluno)
        imgAccount.setOnClickListener(this)

//        initNavigation()

        homeFragment = FragmentHomeAluno()
        accountFragment = FragmentAccountAluno()

        addFragment(homeFragment)


        with(binding){
            imgAccount.setOnClickListener {
                replaceFragment(accountFragment)
                imgHome.setImageDrawable(getDrawable(R.drawable.imghomedes))
                imgAccount.setImageDrawable(getDrawable(R.drawable.imguserbox))
            }
            imgCamera.setOnClickListener {
                val intent = Intent(this@TelaMainAluno, OpenCVTeste::class.java)
                intent.putExtra("nome", nome)
                intent.putExtra("sob", sob)
                intent.putExtra("turma", turma)
                intent.putExtra("email", email)
                intent.putExtra("ra", ra)
                intent.putExtra("inst", inst)
                startActivity(intent)
            }
            imgHome.setOnClickListener {
                replaceFragment(homeFragment)
                imgAccount.setImageDrawable(getDrawable(R.drawable.imgaccountdes))
                imgHome.setImageDrawable(getDrawable(R.drawable.imghome))
            }
        }
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction().add(R.id.fragmentsAluno, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsAluno, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

//    private fun initNavigation(){
//        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentsAluno) as NavHostFragment
//
//        navController = navHostFragment.navController
//
//        NavigationUI.setupWithNavController(binding.bottomNavigationAlunos, navController)
//    }


}