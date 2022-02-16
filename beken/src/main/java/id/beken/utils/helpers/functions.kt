package id.beken.utils.helpers

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import id.beken.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

private val json = Json { ignoreUnknownKeys = true; isLenient = true;  }

fun retrofit(debug: Boolean): Retrofit {
    val baseUrl = if (debug) BuildConfig.BASE_URL_DEV else BuildConfig.BASE_URL_PRO
    val instance = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(
            json.asConverterFactory("application/json".toMediaType())
        )
    return instance.build()
}

fun homeURL(debug: Boolean) = if (debug) BuildConfig.HOME_URL_DEV else BuildConfig.HOME_URL_PRO