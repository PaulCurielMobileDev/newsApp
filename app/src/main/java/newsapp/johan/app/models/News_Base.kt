package newsapp.johan.app.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class News_Base(

    @field:Json(name = "status") val status: String,
    @field:Json(name = "totalResults") val totalResults: Int,
    @field:Json(name = "articles") val articles: List<Articles>?
)

@Parcelize
data class Source(

    @field:Json(name = "id") val id: String?,
    @field:Json(name = "name") val name: String?
) : Parcelable

@Parcelize
data class Articles(

    @field:Json(name = "source") val source: Source?,
    @field:Json(name = "author") val author: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "url") val url: String?,
    @field:Json(name = "urlToImage") val urlToImage: String?,
    @field:Json(name = "publishedAt") val publishedAt: String?,
    @field:Json(name = "content") val content: String?
) : Parcelable