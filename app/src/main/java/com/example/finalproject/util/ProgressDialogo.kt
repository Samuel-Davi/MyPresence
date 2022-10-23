package com.example.finalproject.util

import android.app.ProgressDialog
import android.content.Context

class ProgressDialogo() {
    lateinit var progressDialog: ProgressDialog

    fun messageBox(message:String, context:Context){
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage(message)
        progressDialog.setCancelable(false)
        progressDialog.show()
    }
}