package a24seven.uz.network.models

import com.google.gson.JsonObject
import org.json.JSONObject

data class YandexGeoObject(
    val response: JsonObject
)
data class YandexResponseObject(
    val featureMember: List<FeatureObject>
)

data class FeatureObject(
    val GeoObject : JSONObject
)