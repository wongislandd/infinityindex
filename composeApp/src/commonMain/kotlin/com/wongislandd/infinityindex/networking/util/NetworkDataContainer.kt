package com.wongislandd.infinityindex.networking.util

@kotlinx.serialization.Serializable
data class NetworkDataContainer<T>(
    val offset: Int?,
    val limit: Int?,
    val total: Int?,
    val count: Int?,
    val results: List<T>?
)