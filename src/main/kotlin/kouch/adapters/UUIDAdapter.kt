package kouch.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

object UUIDAdapter {
    class UuidJson(val uuid: String)

    @FromJson
    fun uuidFromJson(json: UuidJson): UUID = UUID.fromString(json.uuid)

    @ToJson
    fun uuidToJson(uuid: UUID) = UuidJson(uuid.toString())
}