package com.zkteco.gitsearchhub.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.zkteco.gitsearchhub.R
import com.zkteco.gitsearchhub.data.model.GitHubUser
import com.zkteco.gitsearchhub.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var repoAdapter: RepoPagingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        val user: GitHubUser? = arguments?.getParcelable("user")
        initializeAdapter()



        user?.let {
            binding.tvUsername.text = it.login
            Glide.with(requireContext()).load(it.avatar_url).into(binding.ivAvatar)
            profileViewModel.fetchUserDetails(it.login)
            observeRepoResults(it.repos_url)
        } ?: run {
            findNavController().navigateUp()
        }

        observeUserDetails()
        onBackButtonClick()


    }

    private fun onBackButtonClick() {
        binding.imgBack.setOnClickListener{
            findNavController().navigateUp()
        }

    }

    private fun initializeAdapter(){
        repoAdapter = RepoPagingAdapter()
        binding.recyclerViewRepos.adapter = repoAdapter
        binding.recyclerViewRepos.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeRepoResults(url: String) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.getRepositories(url).collectLatest { pagingData ->
                    repoAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeUserDetails() {
        profileViewModel.userDetails.observe(viewLifecycleOwner) { userDetails ->
            binding.tvRepositories.text = getString(R.string.repositories_text, userDetails.public_repos)
            binding.tvFollowers.text = getString(R.string.followers_text, userDetails.followers)

            if (userDetails.bio.isNullOrEmpty()) {
                binding.tvBio.visibility = View.GONE
            } else {
                binding.tvBio.text = userDetails.bio
            }
        }
    }
}
