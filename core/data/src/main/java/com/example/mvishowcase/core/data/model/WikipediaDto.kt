package com.example.mvishowcase.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikipediaResponse(
    @SerialName("query")
    val query: WikipediaQueryDto? = null
)

@Serializable
data class WikipediaQueryDto(
    @SerialName("pages")
    val pages: Map<String, WikipediaPageDto>? = null
)

@Serializable
data class WikipediaPageDto(
    @SerialName("pageid")
    val pageid: Int,
    @SerialName("title")
    val title: String,
    @SerialName("extract")
    val extract: String? = null
)
