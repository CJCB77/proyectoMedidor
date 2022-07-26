package com.example.proyectomedidor.Response

data class LoginResponse(
    var message: String? = null,
    var token:String?,
    var userId:Int?,
    var username:String?,
    var role:Int?,
    var loggedIn:Boolean = false
)