package com.parana.dobleyfalta.retrofit

object ApiConstants {
    // Hay que cambiar esta IP según si usás emulador o celular físico
    // Emulador: http://10.0.2.2:8083/
    // Físico: http://<Sus IPS>8083/
    const val BASE_URL_NOTICIAS = "http://192.168.0.56:8083/"
//    El puerto no deberia ser ese, deberia ser el de la apigateway y que te redirija a cada microservicio
}