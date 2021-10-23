package kouch.http

import kotlinx.serialization.json.*
import kouch.database.CouchServer
import kouch.json.CouchResponse
import org.http4k.core.*
import org.http4k.core.cookie.cookies
import org.http4k.filter.cookie.LocalCookie
import javax.security.sasl.AuthenticationException

internal fun CouchServer.request(
    method: Method,
    endpoint: String,
    payload: JsonObject? = null,
    onResponse: (Response) -> Unit) {

    val fullUrl = Uri.of("https://${baseUrl.host}/$endpoint")

    val request = Request(method, fullUrl).body(payload?.toString() ?: "")

    val response = handler(request)
    response.withStatus(Status.FORBIDDEN) {
        authenticate()
        request(method, endpoint, payload, onResponse)
    }

    onResponse(response)

}

private fun CouchServer.authenticate(): CouchResponse {
    val requestJson = buildJsonObject {
        put("name", this@authenticate.login.username)
        put("password", this@authenticate.login.password)
    }

    var responseJson: CouchResponse? = null
    request(Method.POST, "_session/", requestJson) { response ->
        if (response.status == Status.UNAUTHORIZED) throw AuthenticationException("Username or password incorrect!")
        else responseJson = CouchResponse(Json.parseToJsonElement(response.bodyString()).jsonObject)
    }
    return responseJson!!

}