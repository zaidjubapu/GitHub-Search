package com.zkteco.gitsearchhub.network

import com.zkteco.gitsearchhub.data.model.GitHubRepo
import com.zkteco.gitsearchhub.data.model.GitHubUserDetails
import com.zkteco.gitsearchhub.data.model.GitHubUserResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url


interface ApiService {


    @GET(ApiHelper.SEARCH_USER)
    suspend fun searchGitHubUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50,
    ): NetworkResponse<GitHubUserResponse, Error>

    @GET
    suspend fun getRepositories(
        @Url reposUrl: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 50
    ):  NetworkResponse<List<GitHubRepo>, Error>

    @GET(ApiHelper.USER_DETAILS)
    suspend fun getUserDetail(
        @Path("username") username:String
    ): NetworkResponse<GitHubUserDetails, Error>


}


