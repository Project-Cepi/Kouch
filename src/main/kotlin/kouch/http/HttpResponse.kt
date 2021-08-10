package kouch.http

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.ktor.client.request.*
import kouch.Moshi
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.jvm.Throws
import kotlin.reflect.KClass

sealed class HttpResponse(
    private val bodyString: String,
    val responseData: HttpResponseData,
    private val parser: Moshi = Moshi
    ) {
    private val rawJson = JSONObject(bodyString)
    val ok = rawJson.getBoolean("ok")

    fun <T : Any> parse(klass: KClass<T>): T? {
        return parser.adapter(klass.java).fromJson(bodyString)
    }
}