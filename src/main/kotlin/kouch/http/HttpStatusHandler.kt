package kouch.http

import org.http4k.core.Response
import org.http4k.core.Status

inline fun Response.withStatus(vararg statuses: Status, `do`: (Response) -> Unit) {
    if (statuses.contains(status)) run(`do`)
}

class HttpStatusException(status: Status, message: String) : Exception()