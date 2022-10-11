package com.example.finalproject.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.finalproject.R


class ProfHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_prof_home, container, false)

        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val disciplina = data?.getString("disciplina")
        val sob = data?.getString("sob")
        val email = data?.getString("email")

        val txtOla:TextView = view?.findViewById(R.id.txtOla)!!
        txtOla.text = "Olá, $nome"

        val txtDisciplina:TextView = view?.findViewById(R.id.txtDisciplina)!!
        txtDisciplina.text = "Disciplina: $disciplina"

        return view;
    }

}