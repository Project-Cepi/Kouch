package kouch.http

import com.squareup.moshi.Moshi
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kouch.Moshi
import kouch.http.auth.AuthToken
import kouch.http.auth.HttpAuthenticationException
import org.json.JSONObject
import kotlin.jvm.Throws

class CouchDbConnector(
    database: String,
    host: String,
    port: Int = 5984,
    useHttps: Boolean = true,
    useHttp2: Boolean = true,
    followRedirects: Boolean = true,
    private val moshi: Moshi = Moshi
): HttpHandler {
    val client = HttpClient(Java) {
        this.followRedirects = followRedirects
        engine { config {
            version(if (useHttp2) java.net.http.HttpClient.Version.HTTP_2 else java.net.http.HttpClient.Version.HTTP_1_1)
        } }

        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
    }

    val HttpResponseData.bodyString
        get() = body.toString()

    private val url = "${if (useHttps) "https" else "http"}://$host:$port/${database}"

    override suspend fun get(route: String, builder: RequestBuilder): HttpResponse {
        return client.get<HttpResponseData>(url + route, builder).let {
            HttpResponse(it.bodyString, it, moshi)
        }
    }

    override suspend fun put(route: String, payload: JSONObject, builder: RequestBuilder): HttpResponse {
        return client.put<HttpResponseData>(url + route) {
            builder(this)
            body = payload
        }.let { HttpResponse(it.bodyString, it, moshi) }
    }

    override suspend fun post(route: String, payload: JSONObject, builder: RequestBuilder): HttpResponse {
        return client.post(url + route) {
            builder(this)
            body = payload
        }
    }

    override suspend fun delete(route: String, builder: RequestBuilder): HttpResponse {
        return client.delete(url + route, builder)
    }

    @Throws(HttpAuthenticationException::class)
    override suspend fun authenticate(username: String, password: String): HttpResponse {
        val json = JSONObject()
            .put("name", username)
            .put("password", password)
        return post("/_session", json).let {
            if (it.responseData.statusCode == HttpStatusCode.Unauthorized)
                throw HttpAuthenticationException("Invalid username or password!")
            else it
        }
    }
}