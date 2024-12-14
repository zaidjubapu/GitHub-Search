package com.zkteco.gitsearchhub.ui.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zkteco.gitsearchhub.data.model.GitHubUser
import com.zkteco.gitsearchhub.data.repository.SearchUserRepo
import com.zkteco.gitsearchhub.network.NetworkResponse

class GitHubPagingSource(
    private val searchUserRepo: SearchUserRepo,
    private val query: String,
    private val onNoRecordsFound: () -> Unit,
) : PagingSource<Int, GitHubUser>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubUser> {
        val currentPage = params.key ?: 1
        return try {
            val response = searchUserRepo.searchUsers(query, currentPage)
            when (response) {
                is NetworkResponse.Success -> {

                    val users = response.body.items ?: emptyList()

                    if (currentPage == 1 && users.isEmpty()) {
                        onNoRecordsFound()
                        return LoadResult.Page(
                            data = emptyList(),
                            prevKey = null,
                            nextKey = null
                        )
                    }

                    LoadResult.Page(
                        data = users,
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (users.isEmpty()) null else currentPage + 1
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

    override fun getRefreshKey(state: PagingState<Int, GitHubUser>): Int? {
        return state.anchorPosition
    }
}
