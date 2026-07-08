package com.example.mvishowcase.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class WikipediaResponse(
    val query: WikipediaQueryDto? = null
)

@Serializable
data class WikipediaQueryDto(
    val pages: Map<String, WikipediaPageDto>? = null
)

@Serializable
data class WikipediaPageDto(
    val pageid: Int,
    val title: String,
    val extract: String? = null
)
