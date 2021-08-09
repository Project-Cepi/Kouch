package kouch.http

import com.squareup.moshi.JsonAdapter
import io.ktor.client.request.*
import kouch.Moshi
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.jvm.Throws
import kotlin.reflect.KClass

sealed class HttpResponse(val bodyString: String, val responseData: HttpResponseData) {
    fun toStringResponse() = StringResponse(bodyString, responseData)

    @Throws(JSONException::class)
    fun toJsonResponse() = JsonResponse(JSONObject(bodyString), responseData)

    inline fun <reified T : Any> toObjectResponse(): HttpObjectResponse<T> {
        return HttpObjectResponse(bodyString, T::class, responseData)
    }
}

class StringResponse(val body: String, responseData: HttpResponseData) : HttpResponse(body, responseData)

class JsonResponse(val body: JSONObject, responseData: HttpResponseData) : HttpResponse(body.toString(), responseData)

class HttpObjectResponse<T: Any>(json: String, klass: KClass<T>, responseData: HttpResponseData)
    : HttpResponse(json, responseData) {
    val body = Moshi.adapter(klass.java).fromJson(json)
}