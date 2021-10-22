package kouch

import org.http4k.client.AsyncHttpHandler
import org.http4k.client.OkHttp
import org.http4k.core.Uri

class CouchDatabase(
    url: Uri,
    handler: AsyncHttpHandler = OkHttp.invoke()
) : AsyncHttpHandler by handler