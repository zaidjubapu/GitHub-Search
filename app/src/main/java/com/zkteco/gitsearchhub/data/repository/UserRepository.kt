package com.zkteco.gitsearchhub.data.repository

import com.zkteco.gitsearchhub.data.model.GitHubUserDetails
import com.zkteco.gitsearchhub.data.model.GitHubUserResponse
import com.zkteco.gitsearchhub.network.ApiService
import com.zkteco.gitsearchhub.network.NetworkResponse
import com.zkteco.gitsearchhub.network.Error

import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun searchUsers(query: String, pageNo: Int): NetworkResponse<GitHubUserResponse, Error> {
        return apiService.searchGitHubUsers(query, pageNo)
    }

    suspend fun getUserDetail(userName: String): NetworkResponse<GitHubUserDetails, Error> {
        return apiService.getUserDetail(userName)
    }
}
