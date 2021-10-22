package kouch.database
import org.http4k.filter.cookie.BasicCookieStorage

data class CouchUser(val username: String, val password: String, var token: AuthToken)

@JvmInline
value class AuthToken(val token: String)