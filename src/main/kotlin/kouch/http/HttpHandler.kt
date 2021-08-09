package kouch.http

import io.ktor.client.*
import io.ktor.client.request.*

interface HttpHandler {
    val client: HttpClient
    val host: String
    val port: Int
    val useHttps: Boolean

    private val protocol
        get() = if (useHttps) "https" else "http"
    private val url
        get() = "$protocol://$host:$port"

    suspend fun get(route: String, builder: RequestBuilder = {}): HttpResponse {
        val response = client.get<HttpResponseData>(url + route, builder)
        return StringResponse(response.bodyString, response)
    }

    suspend fun post(route: String, builder: RequestBuilder = {}): HttpResponse {
        val response = client.post<HttpResponseData>(url + route, builder)
        return StringResponse(response.bodyString, response)
    }

    private val HttpResponseData.bodyString
        get() = this.body.toString()

}

private typealias RequestBuilder = HttpRequestBuilder.() -> Unit