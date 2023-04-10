package com.macreai.githubapp.data.remote.api

import com.macreai.githubapp.BuildConfig
import com.macreai.githubapp.data.remote.response.DetailUserResponse
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.data.remote.response.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //Mengambil list user
    @GET("/search/users")
    @Headers("Authorization: token ${BuildConfig.TOKEN_KEY}")
    fun getUser(
        @Query("q") q: String
    ): Call<UserResponse>

    //Mengambil detail user
    @GET("/users/{username}")
    @Headers("Authorization: token ${BuildConfig.TOKEN_KEY}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    //Mengambil list following
    @GET("/users/{username}/following")
    @Headers("Authorization: token ${BuildConfig.TOKEN_KEY}")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    //Mengambil list followers
    @GET("/users/{username}/followers")
    @Headers("Authorization: token ${BuildConfig.TOKEN_KEY}")
    fun getUserFollower(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}