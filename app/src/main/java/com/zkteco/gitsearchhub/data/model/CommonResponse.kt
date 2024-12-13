package com.zkteco.gitsearchhub.data.model

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
data class GitHubUserResponse(
    val total_count: Int? = null,
    val incomplete_results: Boolean? = null,
    val items: List<GitHubUser>? = null
)


@JsonClass(generateAdapter = true)
data class GitHubUserDetails(
    val login: String? = null,
    val id: Int? = null,
    val node_id: String? = null,
    val avatar_url: String? = null,
    val gravatar_id: String? = null,
    val url: String? = null,
    val html_url: String? = null,
    val followers_url: String? = null,
    val following_url: String? = null,
    val gists_url: String? = null,
    val starred_url: String? = null,
    val subscriptions_url: String? = null,
    val organizations_url: String? = null,
    val repos_url: String? = null,
    val events_url: String? = null,
    val received_events_url: String? = null,
    val type: String? = null,
    val user_view_type: String? = null,
    val site_admin: Boolean? = null,
    val name: String? = null,
    val company: String? = null,
    val blog: String? = null,
    val location: String? = null,
    val email: String? = null,
    val hireable: Boolean? = null,
    val bio: String? = null,
    val twitter_username: String? = null,
    val public_repos: Int? = null,
    val public_gists: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)




@Parcelize
@JsonClass(generateAdapter = true)
data class GitHubUser(
    val login: String,
    val id: Int,
    val node_id: String,
    val avatar_url: String,
    val gravatar_id: String?,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val starred_url: String,
    val subscriptions_url: String,
    val organizations_url: String,
    val repos_url: String,
    val events_url: String,
    val received_events_url: String,
    val type: String,
    val user_view_type: String,
    val site_admin: Boolean,
    val score: Double?= null
): Parcelable


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









