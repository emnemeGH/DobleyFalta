package com.parana.dobleyfalta.retrofit

object ApiConstants {
    // Hay que cambiar esta IP según si usás emulador o celular físico
    // Emulador: http://10.0.2.2:8080/
    // Físico: http://<Sus IPS>8080/
// 192.168.1.11
    const val BASE_URL = "http://192.168.0.13:8080/"
//    El puerto no deberia ser ese, deberia ser el de la apigateway y que te redirija a cada microservicio
}