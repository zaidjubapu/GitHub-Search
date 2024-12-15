package com.zkteco.gitsearchhub.data.repository

import com.zkteco.gitsearchhub.data.model.GitHubRepo
import com.zkteco.gitsearchhub.network.ApiService
import javax.inject.Inject
import com.zkteco.gitsearchhub.network.Error
import com.zkteco.gitsearchhub.network.NetworkResponse


class RepoRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getRepositories(url: String, pageNo: Int): NetworkResponse<List<GitHubRepo>, Error> {
        return apiService.getRepositories(url, pageNo)
    }
}
