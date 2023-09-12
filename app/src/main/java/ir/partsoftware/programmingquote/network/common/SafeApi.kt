package ir.partsoftware.programmingquote.network.common

import ir.partsoftware.programmingquote.ui.common.Result
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import retrofit2.Response

suspend fun <T> safeApi(
    call: suspend () -> Response<T>,
    onDataReady: (T) -> Unit
): Flow<Result> {
    return flow {
        emit(Result.Loading)
        kotlinx.coroutines.delay(2000)
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (currentCoroutineContext().isActive) {
                        onDataReady(body)
                    }
                    emit(Result.Success)
                } else {
                    emit(Result.Error("whoops body was empty"))
                }
            } else {
                emit(Result.Error("whoops: got ${response.code()} code!"))
            }
        } catch (t: Throwable) {
            emit(Result.Error("whoops: ${t.message}"))
        }
    }.cancellable()
}