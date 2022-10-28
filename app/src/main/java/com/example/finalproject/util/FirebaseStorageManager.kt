package com.example.finalproject.util

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage

class FirebaseStorageManager {
    private val mStorageRef = FirebaseStorage.getInstance().reference
    lateinit var progressDialog: ProgressDialog

    fun uploadImageAdm(context: Context, imageUri: Uri, inst:String){
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Imagem carregando...")
        val uploadTask = mStorageRef.child("Adm/$inst/profilePic.png").putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Toast.makeText(context, "Imagem atualizada", Toast.LENGTH_LONG).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
        }.addOnFailureListener{
            Log.e("TAG", it.message.toString())
        }
    }
}