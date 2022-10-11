package com.example.finalproject.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.finalproject.R
import com.example.finalproject.model.BotaoConfirmado
import com.example.finalproject.databinding.ActivityTelaEscolhaBinding

class TelaEscolha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTelaEscolhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var btConfirm = BotaoConfirmado(false, false, false, 0)

        binding.imgAdm.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtAdm){
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admon))
                btConfirm.valorBtAdm = true
                btConfirm.valorBtProf = false;
                btConfirm.valorBtAluno = false;
                btConfirm.valorConfirm = 3;
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
            }else{
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
                btConfirm.valorBtAdm = false
                btConfirm.valorConfirm = 0;
            }
        }
        binding.imgProf.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtProf){
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profon))
                btConfirm.valorBtProf = true
                btConfirm.valorBtAdm = false
                btConfirm.valorBtAluno = false
                btConfirm.valorConfirm = 2;
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
            }else{
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
                btConfirm.valorBtProf = false
                btConfirm.valorConfirm = 0;
            }
        }
        binding.imgALuno.setOnClickListener {
            Log.d("TAG", btConfirm.valorConfirm.toString())
            if(!btConfirm.valorBtAluno){
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunoon))
                btConfirm.valorBtAluno = true
                btConfirm.valorBtAdm = false
                btConfirm.valorBtProf = false
                btConfirm.valorConfirm = 1;
                binding.imgAdm.setImageDrawable(getDrawable(R.drawable.admoff))
                binding.imgProf.setImageDrawable(getDrawable(R.drawable.profoff))
            }else{
                binding.imgALuno.setImageDrawable(getDrawable(R.drawable.alunooff))
                btConfirm.valorBtAluno = false
                btConfirm.valorConfirm = 0;
            }
        }

        binding.btProx.setOnClickListener {
            if(btConfirm.valorConfirm == 1){
                startActivity(Intent(this, TelaLoginAluno::class.java))
            }else if(btConfirm.valorConfirm == 2) {
                startActivity(Intent(this, TelaLoginProf::class.java))
            }else if(btConfirm.valorConfirm == 3){
                startActivity(Intent(this, TelaLoginAdm::class.java))
            }else{
                Toast.makeText(this, "Por favor, selecione uma das opções para continuar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
