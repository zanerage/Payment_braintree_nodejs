package com.mario_antolovic.payment_braintree_nodejs.Retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var ourInstantce:Retrofit?=null

            val instance:Retrofit
    get() {
        if (ourInstantce == null)
            ourInstantce = Retrofit.Builder()
                .baseUrl("http://10.0.2.0:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return ourInstantce!!
    }
}