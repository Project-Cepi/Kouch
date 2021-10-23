package kouch.database

import org.http4k.client.AsyncHttpHandler
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri
import org.http4k.core.then
import org.http4k.filter.ClientFilters
import org.http4k.filter.cookie.CookieStorage

class CouchServer(
    val baseUrl: Uri,
    val login: CouchUser,
    internal val handler: HttpHandler = OkHttp()
) {
    init { ClientFilters.Cookies().then(handler) }
}