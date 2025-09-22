package org.whosin.client.core.network

sealed interface ApiResult<out T> {
    data class Success<T>(
        val data: T,
        val statusCode: Int? = null
    ) : ApiResult<T>

    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>
}

// 응답이 성공인 경우에 데이터를 매핑할 수 있는 확장함수, 필요시 map의 람다에 매핑 로직을 구현해서 사용
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> = when (this) {
    is ApiResult.Success -> ApiResult.Success(transform(data), statusCode)
    is ApiResult.Error -> this
}
