package kouch.json

import kotlinx.serialization.json.JsonObject

@JvmInline
value class CouchResponse(val json: JsonObject)