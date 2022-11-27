package com.example.finalproject.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalproject.databinding.ActivityTelaEditFotoAdmBinding
import com.example.finalproject.databinding.ActivityTelaEditFotoAlunoBinding
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_tela_edit_foto_adm.*
import kotlinx.android.synthetic.main.activity_tela_edit_foto_aluno.*
import kotlinx.coroutines.selects.select
import java.io.File

class TelaEditFotoAluno : AppCompatActivity() {

    private lateinit var binding: ActivityTelaEditFotoAlunoBinding
    private lateinit var imageUri: Uri
    private lateinit var bitmap: Bitmap
    private lateinit var inst:String
    private lateinit var ra:String
    private lateinit var progressDialog: ProgressDialog
    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaEditFotoAlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val email = data?.getString("email")
        val turma = data?.getString("turma")
        val senha = data?.getString("senha")
        val nome = data?.getString("nome")
        val sobrenome = data?.getString("sob")
        ra = data?.getString("ra").toString()
        Log.d("TAG1", ra)

        inst = data?.getString("inst").toString()
        Log.d("TAG2", inst)

        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/alunos/$ra/$ra.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                binding.imgEditFotoAluno.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }

        with(binding){
            linearEditFotoAluno.setOnClickListener {
                selectImage()
            }
            linearSairTelaOpcAluno.setOnClickListener {
                val intent = Intent(this@TelaEditFotoAluno, TelaMainAluno::class.java)
                intent.putExtra("email", email)
                intent.putExtra("senha", senha)
                intent.putExtra("nome", nome)
                intent.putExtra("inst", inst)
                intent.putExtra("ra", ra)
                intent.putExtra("sob", sobrenome)
                intent.putExtra("turma", turma)
                startActivity(intent);
            }
        }
    }

    private fun selectImage(){
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1)
        } else {
            val galeriIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeriIntext,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val galeriIntext = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeriIntext,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data!!
            if (imageUri != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver,imageUri!!)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    imgEditFotoAluno.setImageBitmap(bitmap)
                    progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Imagem carregando...")
                    val uploadTask = mStorageRef.child("Adm/$inst/alunos/$ra/1.png").putFile(imageUri)
                    uploadTask.addOnSuccessListener {
                        Toast.makeText(this@TelaEditFotoAluno, "Imagem atualizada", Toast.LENGTH_LONG).show()
                        if (progressDialog.isShowing) progressDialog.dismiss()
                    }.addOnFailureListener{
                        Log.e("TAG", it.message.toString())
                        if (progressDialog.isShowing) progressDialog.dismiss()
                    }
                }
                else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageUri)
                    imgEditFoto.setImageBitmap(bitmap)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}