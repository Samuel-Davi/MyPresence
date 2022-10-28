package com.example.finalproject.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.model.Disciplina

class DisciplinaAdapter(var disciplinas:ArrayList<Disciplina>):RecyclerView.Adapter<DisciplinaAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.disciplinascard_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDisc = disciplinas[position]
        holder.textDisciplina.text = currentDisc.nome
        holder.nomeProf.text = "Professor: " + currentDisc.professor
    }

    override fun getItemCount(): Int {
        return  disciplinas.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var layoutConstraint:View = itemView.findViewById(R.id.cardViewDisciplina)
        var textDisciplina:TextView = itemView.findViewById(R.id.txtCardDisciplina)
        var nomeProf:TextView = itemView.findViewById(R.id.txtCardDisciplinaNomeProf)

    }
}