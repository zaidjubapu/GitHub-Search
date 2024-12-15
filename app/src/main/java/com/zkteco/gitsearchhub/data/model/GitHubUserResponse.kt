package com.zkteco.gitsearchhub.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubUserResponse(
    val total_count: Int? = null,
    val incomplete_results: Boolean? = null,
    val items: List<GitHubUser>? = null
)

