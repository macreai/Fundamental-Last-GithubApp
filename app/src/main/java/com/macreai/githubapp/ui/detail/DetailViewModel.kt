package com.macreai.githubapp.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.macreai.githubapp.data.remote.api.ApiConfig
import com.macreai.githubapp.data.remote.response.DetailUserResponse
import com.macreai.githubapp.data.repo.FavRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application): AndroidViewModel(application) {
    private val favRepository: FavRepository = FavRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setUserDetail(user: String?){
        _isLoading.value = true
        val client = user?.let { ApiConfig.getApiService().getUserDetail(it) }
        client?.enqueue(object : Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful){
                    _detailUser.postValue(response.body())
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }

                _isLoading.value = false
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                Log.e(TAG,"onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }

    fun insert(id: Int, username: String?, avatarUrl: String?) {
        favRepository.insert(id, username, avatarUrl)
    }

    fun delete(id: Int, username: String?, avatarUrl: String?) {
        favRepository.delete(id, username, avatarUrl)
    }

    fun getUserByID(id: Int): Int =
        favRepository.getUserById(id)


    companion object {
        const val TAG = "DetailViewModel"
    }

}