package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.model.BotaoConfirmado
import com.example.finalproject.databinding.ActivityTelaEscolhaBinding

class TelaEscolha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaEscolhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btConfirmado = BotaoConfirmado(false, false, false, 0)

        binding.imgAdm.setOnClickListener {
            if(!btConfirmado.valorBtAdm){
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admon))
                btConfirmado.valorBtAdm = true
                btConfirmado.valorConfirm = 3;
                binding.imgALuno.isClickable = false
                binding.imgProf.isClickable = false
            }else{
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
                btConfirmado.valorBtAdm = false
                btConfirmado.valorConfirm = 0;
                binding.imgALuno.isClickable = true
                binding.imgProf.isClickable = true
            }
        }
        binding.imgProf.setOnClickListener {
            if(!btConfirmado.valorBtProf){
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profon))
                btConfirmado.valorBtProf = true
                btConfirmado.valorConfirm = 2;
                binding.imgALuno.isClickable = false
                binding.imgAdm.isClickable = false
            }else{
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
                btConfirmado.valorBtProf = false
                btConfirmado.valorConfirm = 0;
                binding.imgALuno.isClickable = true
                binding.imgAdm.isClickable = true
            }
        }
        binding.imgALuno.setOnClickListener {
            if(!btConfirmado.valorBtAluno){
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunoon))
                btConfirmado.valorBtAluno = true
                btConfirmado.valorConfirm = 1;
                binding.imgAdm.isClickable = false
                binding.imgProf.isClickable = false
            }else{
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                btConfirmado.valorBtAluno = false
                btConfirmado.valorConfirm = 0;
                binding.imgAdm.isClickable = true
                binding.imgProf.isClickable = true
            }
        }

        binding.btProx.setOnClickListener {
            if(btConfirmado.valorConfirm == 1){
                startActivity(Intent(this, TelaLoginAluno::class.java))
            }else if(btConfirmado.valorConfirm == 2) {
                startActivity(Intent(this, TelaLoginProf::class.java))
            }else if(btConfirmado.valorConfirm == 3){
                startActivity(Intent(this, TelaLoginAdm::class.java))
            }else{
                Toast.makeText(this, "Selecione uma opção", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
