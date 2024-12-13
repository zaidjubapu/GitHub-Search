package com.zkteco.gitsearchhub.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.zkteco.gitsearchhub.R
import com.zkteco.gitsearchhub.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch



@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adapter: SearchPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        adapter = SearchPagingAdapter { user ->
            val action = SearchFragmentDirections.actionSearchFragmentToProfileFragment(user)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            searchViewModel.searchQuery.collectLatest { query ->
                if (query.isNotEmpty()) {
                    observeSearchResults(query)
                }
            }
        }

        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.updateSearchQuery(query)  // Update the query
            }
        }
    }
    private fun observeSearchResults(query: String) {
        lifecycleScope.launch {
            searchViewModel.searchUsers(query) {
                Toast.makeText(requireContext(), "No records found", Toast.LENGTH_SHORT).show()

            }.collectLatest { pagingData ->
                adapter.submitData(pagingData)

                adapter.loadStateFlow.collect { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Error -> {
                            val error = (loadState.refresh as LoadState.Error).error
                            Toast.makeText(
                                requireContext(),
                                "Error: ${error.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }






}

