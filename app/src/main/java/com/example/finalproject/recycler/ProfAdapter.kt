package com.example.finalproject.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.CardLayoutBinding
import com.example.finalproject.model.ProfModel
import org.w3c.dom.Text

class ProfAdapter(var prof:ArrayList<ProfModel>): RecyclerView.Adapter<ProfAdapter.ProfViewHolder>() {
    private lateinit var profItemBinding:CardLayoutBinding

    inner class ProfViewHolder(val binding:CardLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfViewHolder {
        profItemBinding = CardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfViewHolder(profItemBinding)
    }

    override fun onBindViewHolder(holder: ProfViewHolder, i: Int) {
        val text = prof[i].nome

        holder.binding.apply {
            itemText.text = text
        }
    }

    override fun getItemCount(): Int {
        return prof.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemText: TextView
        var itemImage: ImageButton

        init{
            itemText = itemView.findViewById(R.id.itemText)
            itemImage = itemView.findViewById(R.id.itemImage)

            itemView.setOnClickListener {
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "você clicou na ${prof[position]}", Toast.LENGTH_LONG).show()
            }
        }
    }

}