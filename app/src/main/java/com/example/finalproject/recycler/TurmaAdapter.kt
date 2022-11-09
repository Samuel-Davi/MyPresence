package com.example.finalproject.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.model.Disciplina
import com.example.finalproject.model.Turma

class TurmaAdapter(var turmas:ArrayList<Turma>): RecyclerView.Adapter<TurmaAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.turmascard_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentDisc = turmas[position]
        holder.nomeTurma.text = currentDisc.nome
        holder.salaTurma.text = "Sala: " + currentDisc.sala
    }

    override fun getItemCount(): Int {
        return  turmas.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var layoutConstraint: View = itemView.findViewById(R.id.cardViewTurma)
        var nomeTurma: TextView = itemView.findViewById(R.id.txtCardTurma)
        var salaTurma: TextView = itemView.findViewById(R.id.txtCardTurmaSala)

    }
}