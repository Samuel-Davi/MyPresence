package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.finalproject.R
import com.example.finalproject.databinding.ActivityTelaMainProfessorBinding
import com.example.finalproject.fragment.FragmentProfHabilita
import com.example.finalproject.fragment.ProfAccountFragment
import com.example.finalproject.fragment.ProfHomeFragment

class TelaMainProfessor : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgHome: ImageView
    private lateinit var imgHabilita:ImageView
    private lateinit var imgAccount: ImageView

    private lateinit var homeFragment: ProfHomeFragment
    private lateinit var habilitaFragment:FragmentProfHabilita
    private lateinit var accountFragment: ProfAccountFragment

    private lateinit var nome:String
    private lateinit var sob:String
    private lateinit var disciplina:String
    private lateinit var email:String
    private lateinit var inst:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaMainProfessorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imgHome = findViewById(R.id.imgHomeProf)
        imgHome.setOnClickListener(this)

        imgAccount = findViewById(R.id.imgAccountProf)
        imgAccount.setOnClickListener(this)

        imgHabilita = findViewById(R.id.imgHabilita)
        imgHabilita.setOnClickListener(this)

        homeFragment = ProfHomeFragment()
        habilitaFragment = FragmentProfHabilita()
        accountFragment = ProfAccountFragment()

        val fragmentTransaction =
            supportFragmentManager.beginTransaction().add(R.id.fragmentsProf, homeFragment)
        fragmentTransaction.commit()

        val data = intent.extras
        nome = data?.getString("nome").toString()
        disciplina = data?.getString("disciplina").toString()
        sob = data?.getString("sob").toString()
        email = data?.getString("email").toString()
        inst = data?.getString("inst").toString()


    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsProf, fragment)
        fragmentTransaction.commit()
    }

    override fun onClick(v: View) {
        var imgHome:ImageView = findViewById(R.id.imgHomeProf)
        var imgAccount:ImageView = findViewById(R.id.imgAccountProf)
        when(v.id){
            R.id.imgAccountProf ->{
                replaceFragment(accountFragment)
                imgHome.setImageDrawable(getDrawable(R.drawable.imghomedes))
                imgAccount.setImageDrawable(getDrawable(R.drawable.imguserbox))
            }
            R.id.imgHomeProf ->{
                replaceFragment(homeFragment)
                imgHome.setImageDrawable(getDrawable(R.drawable.imghome))
                imgAccount.setImageDrawable(getDrawable(R.drawable.imgaccountdes))
            }
            R.id.imgHabilita ->{
                val intent = Intent(this, ProfHabilita::class.java)
                intent.putExtra("inst", inst)
                intent.putExtra("nome", nome);
                intent.putExtra("disciplina", disciplina)
                intent.putExtra("sob", sob)
                intent.putExtra("email", email)
                startActivity(intent)
            }
        }
    }
}