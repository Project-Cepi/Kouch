package kouch.http

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kouch.http.auth.AuthToken
import org.json.JSONObject

interface HttpHandler {

    suspend fun get(route: String, builder: RequestBuilder = {}): HttpResponse

    suspend fun post(route: String, payload: JSONObject, builder: RequestBuilder = {}): HttpResponse

    suspend fun delete(route: String, builder: RequestBuilder = {}): HttpResponse

    suspend fun put(route: String, payload: JSONObject, builder: RequestBuilder = {}): HttpResponse

    suspend fun authenticate(username: String, password: String): AuthToken
}

typealias RequestBuilder = HttpRequestBuilder.() -> Unit