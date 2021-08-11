package kouch.http

import com.squareup.moshi.Moshi
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.features.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kouch.Moshi
import kouch.annotations.KouchDsl
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
        return client.get<HttpResponseData>(url + route) {
            apply(builder)
            addCouchHeaders()
        }.let { HttpResponse(it.bodyString, it, moshi) }
    }

    override suspend fun put(route: String, payload: JSONObject, builder: RequestBuilder): HttpResponse {
        return client.put<HttpResponseData>(url + route) {
            apply(builder)
            addCouchHeaders()
            body = payload
        }.let { HttpResponse(it.bodyString, it, moshi) }
    }

    override suspend fun post(route: String, payload: JSONObject, builder: RequestBuilder): HttpResponse {
        return client.post(url + route) {
            apply(builder)
            addCouchHeaders()
            body = payload
        }
    }

    override suspend fun delete(route: String, builder: RequestBuilder): HttpResponse {
        return client.delete<HttpResponseData>(url + route) {
            apply(builder)
            addCouchHeaders()
        }.let { HttpResponse(it.bodyString, it, moshi) }
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

    @KouchDsl
    class Builder {
        var database: String? = null
        var host: String? = null
        var port: Int = 5984
        var useHttps = true
        var useHttp2 = true
        var followRedirects = true
        var moshi = Moshi

        fun build(): CouchDbConnector {
            val database = requireNotNull(this.database)
            val host = requireNotNull(host)
            return CouchDbConnector(database, host, port, useHttps, useHttp2, followRedirects, moshi)
        }
    }

    companion object {
        fun HttpRequestBuilder.addCouchHeaders() = headers {
            append("Accept", "application/json")
            append("Content-type", "application/json")
        }
    }
}