package com.zkteco.gitsearchhub.ui.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

        setupRecyclerView()
        observeToastMessages()
        observeSearchResults()
        onTextChanged()


    }

    private fun onTextChanged(){
        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            if (query.isNotEmpty()) {
                searchViewModel.updateSearchQuery(query)
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchPagingAdapter { user ->
            val action = SearchFragmentDirections.actionSearchFragmentToProfileFragment(user)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeToastMessages() {
        searchViewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                showToast(it)
            }
        }
    }

    private fun observeSearchResults() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.searchResults.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                    observeLoadState()
                }
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                when (loadState.refresh) {
                    is LoadState.Error -> {
                        val error = (loadState.refresh as LoadState.Error).error
                        showToast("Error: ${error.localizedMessage}")
                    }
                    else -> {
                        Log.d("Pagination", "Load State: ${loadState.refresh}")
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

