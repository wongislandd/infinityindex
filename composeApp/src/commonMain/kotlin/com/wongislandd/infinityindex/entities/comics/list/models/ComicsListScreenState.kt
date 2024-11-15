package com.wongislandd.infinityindex.entities.comics.list.models

import androidx.paging.PagingData
import com.wongislandd.infinityindex.entities.comics.details.models.Comic
import kotlinx.coroutines.flow.StateFlow

enum class SearchIntention {
    PENDING,
    SUBMITTED
}

data class SearchQuery(
    val text: String = "",
    val intention: SearchIntention = SearchIntention.PENDING
)

data class SearchState(
    val searchQuery: SearchQuery,
    val isSearchBoxVisible: Boolean
)