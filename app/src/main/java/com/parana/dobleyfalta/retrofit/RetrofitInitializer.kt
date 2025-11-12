package com.parana.dobleyfalta.retrofit

import android.content.Context
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientNoticias
import com.parana.dobleyfalta.retrofit.clients.RetrofitClientUsuarios

object RetrofitInitializer {
    fun initAll(context: Context) {
        RetrofitClientNoticias.init(context)
        RetrofitClientUsuarios.init(context)
        // RetrofitClientEquipos.init(context)
    }
}