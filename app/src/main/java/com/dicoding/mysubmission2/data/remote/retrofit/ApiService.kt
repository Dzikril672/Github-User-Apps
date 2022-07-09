package com.dicoding.mysubmission2.data.remote.retrofit

import com.dicoding.mysubmission2.data.remote.response.ItemsSearch
import com.dicoding.mysubmission2.data.remote.response.ResponseSearch
import com.dicoding.mysubmission2.data.remote.response.ResponseSearchDetail
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("search/users")
    fun getUsers(
        @Query("q") username: String
    ): Call<ResponseSearch>

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String
    ): ResponseSearchDetail

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String?
    ): Call<List<ItemsSearch>>

//    @Headers("Authorization: token ${BuildConfig.GITHUB_TOKEN}")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String?
    ): Call<List<ItemsSearch>>
}