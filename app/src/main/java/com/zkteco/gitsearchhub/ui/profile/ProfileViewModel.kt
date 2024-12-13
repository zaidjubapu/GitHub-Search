package com.zkteco.gitsearchhub.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zkteco.gitsearchhub.data.model.GitHubRepo
import com.zkteco.gitsearchhub.data.model.GitHubUserDetails
import com.zkteco.gitsearchhub.data.repository.SearchUserRepo
import com.zkteco.gitsearchhub.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val searchUserRepo: SearchUserRepo
) : ViewModel() {

    private val _userDetails = MutableLiveData<GitHubUserDetails>()
    val userDetails: LiveData<GitHubUserDetails> get() = _userDetails



    fun getRepositories(url: String): Flow<PagingData<GitHubRepo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RepoPagingSource(searchUserRepo, url) }
        ).flow.cachedIn(viewModelScope)
    }

    fun fetchUserDetails(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = searchUserRepo.getUserDetail(username)
                when (response) {
                    is NetworkResponse.Success -> {
                        response.body.let {
                            _userDetails.postValue(it)
                        }
                    }
                    is NetworkResponse.ApiError -> {
                        Log.e("ProfileViewModel", "API Error: ${response.body}")
                    }
                    is NetworkResponse.NetworkError -> {
                        Log.e("ProfileViewModel", "Network Error")
                    }
                    is NetworkResponse.UnknownError -> {
                        Log.e("ProfileViewModel", "Unknown Error: ${response.error?.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Exception: ${e.message}")
            }
        }
    }



}
