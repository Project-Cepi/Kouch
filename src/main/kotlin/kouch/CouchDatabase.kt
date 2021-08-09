package kouch

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.http.*
import kouch.http.AuthToken
import kouch.http.AuthenticationException
import kouch.http.HttpHandler
import kouch.http.HttpResponse
import org.json.JSONObject

class CouchDatabase(
    override val databaseId: String,
    override val host: String,
    override val port: Int = 5294,
    override val useHttps: Boolean = true,
    useHttp2: Boolean = true,
) : HttpHandler {
    override val client = HttpClient(Java) {
        followRedirects = true
        engine {
            config {
                version(if (useHttp2) java.net.http.HttpClient.Version.HTTP_2 else java.net.http.HttpClient.Version.HTTP_1_1)

            }
        }
    }

    override lateinit var token: String


    suspend fun authenticate(username: String, password: String): AuthToken {
        val loginResponse = post("/_session") {
            body = JSONObject()
                .append("name", username)
                .append("password", password)
        }.responseData
        if (loginResponse.statusCode == HttpStatusCode.Unauthorized) {
            throw AuthenticationException("Invalid username or password!")
        } else {
            val token = loginResponse.headers["Set-Cookie"]!!.split("; ")[0].split("=")[1]
            return AuthToken(token)
        }
    }
}