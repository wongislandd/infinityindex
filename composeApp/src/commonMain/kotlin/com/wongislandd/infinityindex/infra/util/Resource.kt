package com.wongislandd.infinityindex.infra.util

sealed class Resource<out T> {

    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val error: com.wongislandd.infinityindex.infra.util.Error?, val throwable: Throwable? = null) : Resource<Nothing>()

    fun <R> map(transform: (T) -> R?): Resource<R> {
        return when (this) {
            is Success -> {
                val transformedData = transform(data)
                if (transformedData != null) {
                    Success(transformedData)
                } else {
                    Error(ClientError.TRANSFORMATION_ERROR)
                }
            }
            is Error -> this
            is Loading -> this
        }
    }
}