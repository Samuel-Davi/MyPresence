package com.example.finalproject.recycler

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.model.Prof
import com.example.finalproject.view.TelaAdmProf

class ProfAdapter(var profs:ArrayList<Prof>): RecyclerView.Adapter<ProfAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentProf = profs[position]
        holder.itemText.text = currentProf.nome + " " + currentProf.sobrenome
        holder.itemTextDisciplina.text = currentProf.disciplina

        holder.constraintLayout.setOnClickListener {
            if (holder.layoutDisciplina.isVisible){
                holder.layoutDisciplina.isVisible = false
                holder.itemImage.setImageResource(R.drawable.img_btnext)
            }else{
                holder.layoutDisciplina.isVisible = true
                holder.itemImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return  profs.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemText: TextView = itemView.findViewById(R.id.itemText)
        var itemImage: ImageButton = itemView.findViewById(R.id.itemImage)
        var layoutDisciplina = itemView.findViewById<View>(R.id.layoutDisciplina)
        var constraintLayout:View = itemView.findViewById(R.id.constraintLayout)
        var itemTextDisciplina:TextView = itemView.findViewById(R.id.itemTxtDisc)

    }

}