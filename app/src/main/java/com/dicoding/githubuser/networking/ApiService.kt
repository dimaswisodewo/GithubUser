package com.dicoding.githubuser.networking

import com.dicoding.githubuser.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers(
        "Accept: application/vnd.github.v3+json",
        "Authorization: token ${ApiConfig.GITHUB_TOKEN}"
    )
    @GET("search/users")
    fun getSearchedUser(
        @Query("q") username: String
    ): Call<UserSearchResponse>

    @GET("users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}