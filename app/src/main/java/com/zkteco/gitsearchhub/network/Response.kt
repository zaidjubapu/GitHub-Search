package com.zkteco.gitsearchhub.network


import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class Success(
    @field:Json(name = "id")
    val id: String,

    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "avatar")
    val avatar: String
)

@Keep
@JsonClass(generateAdapter = true)
data class Error(
    @Json(name = "status")
    val status: String? = null,

    @Json(name = "code")
    val code: String? = null,

    @Json(name = "message")
    val message: String? = null
)
