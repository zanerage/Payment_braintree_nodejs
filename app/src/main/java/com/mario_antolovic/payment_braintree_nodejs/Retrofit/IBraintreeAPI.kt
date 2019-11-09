package com.mario_antolovic.payment_braintree_nodejs.Retrofit

import com.mario_antolovic.payment_braintree_nodejs.Model.BraintreeToken
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.*

interface IBraintreeAPI {
    @get:GET("checkouts/new")
    val token: Observable<BraintreeToken>

    @POST("checkouts")
    @FormUrlEncoded
    fun  submitPayment (@Field("amount") amount:String,
                        @Field("payment_method_nonce") nonce:String):Observable<BraintreeTransaction>
}