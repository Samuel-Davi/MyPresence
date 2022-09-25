package com.example.finalproject.network

import com.example.finalproject.model.AdmModel
import com.example.finalproject.model.ProfModel
import com.example.finalproject.model.UserModel
import com.google.firebase.firestore.auth.User
import retrofit2.Call
import retrofit2.http.*


interface InterfaceApi {
    @FormUrlEncoded
    @POST("usuario/")
    fun createAdm(
        @Field("nome") nome: String,
        @Field("senha") senha: String,
        @Field("email") email: String,
        @Field("instituicao") instituicao: String,
        @Field("tipo") tipo: Int
    ): Call<AdmModel>

    @FormUrlEncoded
    @POST("usuario/")
    fun createProf(
        @Field("nome") nome:String,
        @Field("senha") senha: String,
        @Field("disciplina") disciplina:String,
        @Field("email") email:String,
        @Field("tipo") tipo:Int
    ): Call<ProfModel>
    @GET("usuario/adm")
    fun authAdm(
        @Query("email") email: String,
        @Query("senha") senha: String,
    ):Call<AdmModel>
    @GET("usuario/professor")
    fun authProf(
        @Query("email") email: String,
        @Query("senha") senha: String,
    ):Call<ProfModel>
    @GET("usuario/professores")
    fun getProfs(
        @Query("email") email: String,
        @Query("nome") nome: String,
        @Query("senha") senha: String,
        @Query("disciplina") disciplina: String
    ):Call<List<ProfModel>>
    @PUT("usuario/")
    fun updateUser(
        @Field("email") email: String,
        @Field("senha") senha: String
    ): Call<UserModel>
    @DELETE("usuario")
    fun deleteUser(
        @Query("email") email: String,
        @Query("senha") senha: String
    ):Call<UserModel>
}
