package com.holker.smart.functionalities.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.holker.smart.data.model.Token
import com.holker.smart.data.model.UserDetailedInfo
import com.holker.smart.data.model.UserTokenInfo
import com.holker.smart.data.repository.SmartAdApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class LoginVM @Inject constructor(var service: SmartAdApiService) : ViewModel() {
    private val _TAG = LoginVM::class.java.name

    var event = MutableLiveData<LoginState>()

    var emailObservable = ObservableField<String>("")
    val passwordObservable = ObservableField<String>("")

    init {
        event.value = LoginState.Wait
    }

    fun login() {
        val userTokenInfo = UserTokenInfo(
            emailObservable.get()!!,
            passwordObservable.get()!!
        )
        val callCreateToken = service.postCreateToken(userTokenInfo)
        callCreateToken.enqueue(object : Callback<Token> {
            override fun onFailure(call: Call<Token>, t: Throwable) {
                Log.e(_TAG, "Error while creating token. Error : ${t.message}")
                event.value = LoginState.Error("${t.message}")
            }

            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                Log.i(_TAG, response.message())
                when (response.code()) {
                    400 -> {
                        Log.e(_TAG, "Received 400 response. Wrong credentials.")
                        event.value = LoginState.Error("Email or password is wrong.")
                    }
                    200 -> {
                        Log.i(_TAG, "Response 200 while creating token.")
                        val token = response.body()?.token!!
                        Log.i(_TAG, "Token : $token")
                        getUserDetails(token)
                    }
                }
            }
        })
    }

    fun getUserDetails(token: String) {
        val callUserDetails = service.getUserInfo("Token $token")
        callUserDetails.enqueue(object : Callback<UserDetailedInfo> {
            override fun onFailure(call: Call<UserDetailedInfo>, t: Throwable) {
                Log.e(_TAG, "Error while getting user details based on token. Error : ${t.message}")
                event.value = LoginState.Error("Server error. Please try later.")
            }

            override fun onResponse(
                call: Call<UserDetailedInfo>,
                response: Response<UserDetailedInfo>
            ) {
                when (response.code()) {
                    200 -> {
                        Log.i(
                            _TAG,
                            "Received 200 while getting user details. Sending info to Activity"
                        )
                        event.value = LoginState.LoginSuccess(token, response.body()!!)
                    }
                    401 -> {
                        Log.i(
                            _TAG,
                            "Wrong token while getting a user details. It's impossible, to be honest"
                        )
                        event.value = LoginState.Error("Server error. Please try again.")
                    }
                    else -> {
                        Log.e(_TAG, "Unhandled response code : ${response.code()}")
                    }
                }
            }

        })
    }

    fun signUp() {
        event.value = LoginState.SignUp
    }
}