package kouch.http.exceptions

import io.ktor.client.request.*
import io.ktor.http.*

class HttpStatusException(val code: HttpStatusCode, override val message: String? = "") : Exception()

fun checkExceptions(error: String, data: HttpResponseData, vararg expected: HttpStatusCode) {
    if (!expected.contains(data.statusCode)) throw HttpAuthenticationException(error)
}

fun checkExceptions(data: HttpResponseData, vararg expected: HttpStatusCode) {
    if (!expected.contains(data.statusCode)) throw HttpAuthenticationException(null)
}

fun checkExceptions(data: HttpResponseData, vararg expected: Pair<HttpStatusCode, String>) {
    expected.firstOrNull { it.first == data.statusCode }?.let {
        throw HttpAuthenticationException(it.second)
    }
}