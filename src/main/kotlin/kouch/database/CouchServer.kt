package kouch.database

import org.http4k.client.AsyncHttpHandler
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

class CouchServer(
    val baseUrl: Uri,
    val login: CouchUser,
    internal val handler: HttpHandler = OkHttp()
)