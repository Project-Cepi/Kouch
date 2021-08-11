package kouch.http.exceptions

import io.ktor.http.*

class HttpStatusException(val code: HttpStatusCode, override val message: String? = "") : Exception()