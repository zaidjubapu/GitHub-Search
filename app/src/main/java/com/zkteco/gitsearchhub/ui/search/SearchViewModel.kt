package com.zkteco.gitsearchhub.ui.search


import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zkteco.gitsearchhub.R
import com.zkteco.gitsearchhub.data.model.GitHubUser
import com.zkteco.gitsearchhub.data.repository.UserRepository
import com.zkteco.gitsearchhub.utility.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val resources: Resources
) : ViewModel() {

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> = _toastMessage


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _searchResults = MutableStateFlow<PagingData<GitHubUser>>(PagingData.empty())
    val searchResults: StateFlow<PagingData<GitHubUser>> = _searchResults

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query.isNotEmpty()) {
                        searchUsers(query)
                            .collectLatest { pagingData ->
                                _searchResults.value = pagingData
                            }
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun searchUsers(query: String): Flow<PagingData<GitHubUser>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                GitHubPagingSource(userRepository, query) {
                    _toastMessage.postValue(resources.getString(R.string.no_records_found))
                }
            }
        ).flow.cachedIn(viewModelScope)
    }
}

