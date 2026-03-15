package edu.nd.pmcburne.hello

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class VisualCenter(
    val latitude: Double,
    val longitude: Double
)

data class PlacemarkResponse(
    val id: Int,
    val name: String,
    @SerializedName("tag_list") val tagList: List<String>,
    val description: String,
    @SerializedName("visual_center") val visualCenter: VisualCenter
)

interface PlacemarkApi {
    @GET("placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkResponse>

    companion object {
        private const val BASE_URL = "https://www.cs.virginia.edu/~wxt4gm/"

        fun create(): PlacemarkApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PlacemarkApi::class.java)
        }
    }
}
