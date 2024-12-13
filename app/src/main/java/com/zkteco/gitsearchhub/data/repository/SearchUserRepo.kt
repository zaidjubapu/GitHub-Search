package com.zkteco.gitsearchhub.data.repository

import com.zkteco.gitsearchhub.data.model.GitHubRepo
import com.zkteco.gitsearchhub.data.model.GitHubUserDetails
import com.zkteco.gitsearchhub.data.model.GitHubUserResponse
import com.zkteco.gitsearchhub.network.ApiService
import com.zkteco.gitsearchhub.network.Error
import com.zkteco.gitsearchhub.network.NetworkResponse
import javax.inject.Inject

class SearchUserRepo @Inject constructor(
    private val apiService: ApiService,

    ) {

    suspend fun searchUsers(query: String,pageNo:Int): NetworkResponse<GitHubUserResponse, Error> {
        return apiService.searchGitHubUsers(query,pageNo)
    }

    suspend fun getRepositories(url:String,pageNo:Int): NetworkResponse<List<GitHubRepo>, Error> {
        return apiService.getRepositories(url,pageNo)
    }

    suspend fun getUserDetail(userName:String): NetworkResponse<GitHubUserDetails, Error> {
        return apiService.getUserDetail(userName)
    }

}