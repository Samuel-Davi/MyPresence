package com.example.finalproject.model

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("email")
    var email:String?,
    @SerializedName("senha")
    var senha:String?)
