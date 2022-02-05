package id.beken.data.services

import androidx.lifecycle.ViewModelProvider
import id.beken.BuildConfig
import id.beken.utils.helpers.retrofit
import retrofit2.Response
import retrofit2.http.*

interface TransactionService : ViewModelProvider.Factory {
    companion object {
        fun create(): TransactionService {
            return retrofit(BuildConfig.BASE_URL).create(TransactionService::class.java)
        }
    }

    @Headers("Content-Type: application/json")
    @POST
    suspend fun trx(
        @Url url: String,
        @Header("Authorization") signature: String,
        @Header("public-key") publicKey: String,
        @Body body: HashMap<String, String?>
    ): Response<String>
}