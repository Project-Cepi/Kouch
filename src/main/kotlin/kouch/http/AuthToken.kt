package kouch.http

@JvmInline
value class AuthToken(val token: String)

class AuthenticationException(override val message: String?) : Exception()