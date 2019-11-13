package com.mario_antolovic.payment_braintree_nodejs

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.mario_antolovic.payment_braintree_nodejs.Model.BraintreeTransaction
import com.mario_antolovic.payment_braintree_nodejs.Retrofit.IBraintreeAPI
import com.mario_antolovic.payment_braintree_nodejs.Retrofit.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE: Int = 1234
    internal var token:String?=null
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var myAPI : IBraintreeAPI





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Init
        myAPI = RetrofitClient.instance.create(IBraintreeAPI::class.java)

        //View
        btn_pay.setOnClickListener { submitPayment() }

        // Load the Token
        compositeDisposable.add(myAPI.token.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ braintreeToken ->
                if (braintreeToken.success) {
                    token = braintreeToken.clientToken

                    waiting_group.visibility = View.GONE
                    payment_group.visibility = View.VISIBLE




                }

            },{throwable -> Toast.makeText(this@MainActivity,""+ throwable.message,Toast.LENGTH_SHORT).show()}))




            }



    private fun submitPayment() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

        val dropInRequest = DropInRequest().clientToken(token)
        startActivityForResult(dropInRequest.getIntent(this),REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                val nonce = result.paymentMethodNonce!!.nonce
                if (!TextUtils.isEmpty(edt_amount.text.toString())) {
                    compositeDisposable.add(myAPI.submitPayment(edt_amount.text.toString(),
                        nonce)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe ({
                            t:BraintreeTransaction? ->
                            if (t!!.succcess) {
                                Toast.makeText(this@MainActivity,"" + t.transaction!!.id,Toast.LENGTH_SHORT).show()
                            }
                        },{t:Throwable? -> Toast.makeText(this@MainActivity,"" + t!!.message,Toast.LENGTH_SHORT).show()}))
                }

            }
        }
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()

    }

}
