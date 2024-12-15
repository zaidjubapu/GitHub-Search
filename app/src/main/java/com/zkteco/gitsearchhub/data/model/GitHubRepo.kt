package com.zkteco.gitsearchhub.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GitHubRepo(
    val id: Long? = null,
    val node_id: String? = null,
    val name: String? = null,
    val full_name: String? = null,
    val private: Boolean? = null,
    val owner: GitHubUser? = null,
    val html_url: String? = null,
    val description: String? = null,
    val fork: Boolean? = null,
    val url: String? = null,
    val forks_url: String? = null,
    val keys_url: String? = null,
    val collaborators_url: String? = null,
    val teams_url: String? = null,
    val hooks_url: String? = null,
    val issue_events_url: String? = null,
    val events_url: String? = null,
    val assignees_url: String? = null,
    val branches_url: String? = null,
    val tags_url: String? = null,
    val blobs_url: String? = null,
    val git_tags_url: String? = null,
    val git_refs_url: String? = null,
    val trees_url: String? = null,
    val stargazers_count:Int? = null

)

