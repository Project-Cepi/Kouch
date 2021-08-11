package kouch

import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.http.*
import kouch.annotations.KouchDsl
import kouch.http.*
import org.json.JSONObject

open class CouchDatabase(
    val dbName: String,
    val connector: CouchDbConnector,
    username: String,
    password: String
) : HttpHandler by connector {
    @KouchDsl
    class Builder {
        var dbName: String? = null
        var username: String? = null
        var password: String? = null
        private lateinit var connector: CouchDbConnector

        fun connector(builder: CouchDbConnector.Builder.() -> Unit) {
            connector = CouchDbConnector.Builder().apply(builder).build()
        }

        fun build(): CouchDatabase {
            val name = requireNotNull(this.dbName)
            val username = requireNotNull(this.username)
            val password = requireNotNull(this.password)

            return CouchDatabase(name, connector, username, password)
        }
    }
}