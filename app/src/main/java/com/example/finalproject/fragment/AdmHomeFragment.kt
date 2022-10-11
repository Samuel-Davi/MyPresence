package com.example.finalproject.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.finalproject.R


class AdmHomeFragment : Fragment() {

//    private lateinit var buttonProfs:TextView
//    private lateinit var buttonTurmas:TextView
//
    private lateinit var profsFragment: FragmentProfs
    private lateinit var turmasFragment: FragmentTurmas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_adm_home, container, false)

        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val email = data?.getString("email")

        val txtOla:TextView = view.findViewById(R.id.txtOla)
        txtOla.text = "Olá, $nome"

        val buttonProfs: TextView = view.findViewById(R.id.txtProf)

        val buttonTurmas: TextView = view.findViewById(R.id.txtTurma)

        profsFragment = FragmentProfs()
        turmasFragment = FragmentTurmas()

        addFragment(profsFragment)

        buttonProfs?.setOnClickListener {
            replaceFragment(profsFragment)
            Log.d("TAG", "ta aq")
        }
        buttonTurmas?.setOnClickListener {
            replaceFragment(turmasFragment)
            Log.d("TAG", "agr ta aq")
        }

        return view
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction().add(R.id.fragmentsAdmHome, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsAdmHome, fragment)
        fragmentTransaction.commit()
    }
}