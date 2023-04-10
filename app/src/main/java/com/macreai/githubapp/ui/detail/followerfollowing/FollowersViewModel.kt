package com.macreai.githubapp.ui.detail.followerfollowing

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.macreai.githubapp.data.remote.api.ApiConfig
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.ui.detail.DetailViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel:ViewModel() {

    private var _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setListFollower(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollower(username)
        client.enqueue(object : Callback<List<ItemsItem>>{
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                if (response.isSuccessful){
                    _listFollowers.postValue(response.body())
                } else {
                    Log.e(DetailViewModel.TAG,"onFailure: ${response.message()}")
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }

    companion object {
        const val TAG = "FollowersViewModel"
    }
}