package kouch.database

import org.http4k.client.AsyncHttpHandler
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Uri

class CouchServer(
    url: Uri,
    login: CouchUser,
    handler: AsyncHttpHandler = OkHttp()
): AsyncHttpHandler by handler {

}