package id.beken.utils.helpers

import id.beken.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun retrofit(debug: Boolean): Retrofit {
    val baseUrl = if (debug) BuildConfig.BASE_URL_DEV else BuildConfig.BASE_URL_PRO
    val instance = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
    return instance.build()
}

fun homeURL(debug: Boolean) = if (debug) BuildConfig.HOME_URL_DEV else BuildConfig.HOME_URL_PRO