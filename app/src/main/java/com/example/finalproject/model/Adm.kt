package com.example.finalproject.model

class Adm(){

    var nome:String = ""
    var senha:String = ""
    var email:String = ""
    var instituicao:String = ""



    constructor(nome: String, email: String, senha: String, instituicao:String) : this() {
        this.nome = nome
        this.email = email
        this.senha = senha
        this.instituicao = instituicao
    }
}
