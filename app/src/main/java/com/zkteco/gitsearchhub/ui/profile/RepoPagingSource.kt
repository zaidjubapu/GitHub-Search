package com.zkteco.gitsearchhub.ui.profile

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zkteco.gitsearchhub.data.model.GitHubRepo
import com.zkteco.gitsearchhub.data.repository.RepoRepository
import com.zkteco.gitsearchhub.network.NetworkResponse


class RepoPagingSource(
    private val repoRepository: RepoRepository,
    private val reposUrl: String
) : PagingSource<Int, GitHubRepo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubRepo> {
        val currentPage = params.key ?: 1
        return try {
            val page = params.key ?: 1
            when (val response = repoRepository.getRepositories(reposUrl, page)) {
                is NetworkResponse.Success -> {
                    val repos = response.body ?: emptyList()
                    Log.d("list", "load: thie items are ${repos.size}")
                    LoadResult.Page(
                        data = repos,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (repos.isEmpty()) null else currentPage + 1
                    )
                }
                is NetworkResponse.ApiError -> {
                    LoadResult.Error(Throwable("API Error: ${response.body.message}"))
                }
                is NetworkResponse.NetworkError -> {
                    LoadResult.Error(Throwable("Network Error"))
                }
                is NetworkResponse.UnknownError -> {
                    LoadResult.Error(Throwable("Unknown Error"))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubRepo>): Int? {
        return state.anchorPosition
    }
}

