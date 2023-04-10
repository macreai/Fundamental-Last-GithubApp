package com.macreai.githubapp.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.macreai.githubapp.data.remote.api.ApiConfig
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    private val _listUser = MutableLiveData<List<ItemsItem>>()
    val listUser: LiveData<List<ItemsItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNotFound = MutableLiveData<Boolean>()
    val isNotFound: LiveData<Boolean> = _isNotFound

    init {
        findUsers("arda")
    }

    fun findUsers(query: String){
        _isLoading.value = true
        _isNotFound.value = false
        val client = ApiConfig.getApiService().getUser(query)
        client.enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful){
                    _listUser.postValue(response.body()?.items)
                    Log.d(TAG, "onResponse: ${_listUser.value}")
                    if (response.body()?.items.isNullOrEmpty()){
                        _isNotFound.value = true
                    }
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }



    companion object {
        const val TAG = "MainViewModel"
    }

}