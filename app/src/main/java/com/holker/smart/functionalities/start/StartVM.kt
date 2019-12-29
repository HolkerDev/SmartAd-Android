package com.holker.smart.functionalities.start

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.holker.smart.data.model.UserDetailedInfo
import com.holker.smart.data.repository.SmartAdApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class StartVM @Inject constructor(var service: SmartAdApiService) : ViewModel() {

    private val TAG = StartVM::class.java.name

    var event = MutableLiveData<StartState>()

    init {
        event.value = StartState.Wait
    }

    fun validateToken(token: String) {
        if (token == "") {
            Log.i(TAG, "Auth token is empty. Return Login signal.")
            event.value = StartState.Login
        } else {
            Log.i(TAG, "Auth token is not empty. Sending request to API.")
            val callUserDetails = service.getUserInfo("Token $token")
            callUserDetails.enqueue(object : Callback<UserDetailedInfo> {
                override fun onFailure(call: Call<UserDetailedInfo>, t: Throwable) {
                    Log.e(TAG, "Error while checking token : ${t.message}")
                    event.value = StartState.Error("Authorization error")
                }

                override fun onResponse(
                    call: Call<UserDetailedInfo>,
                    response: Response<UserDetailedInfo>
                ) {
                    if (response.code() == 200)
                        event.value = StartState.TokenSuccess(response.body()!!)
                    else
                        Log.i(TAG, "Status code is not 200")
                }
            })
        }
    }
}