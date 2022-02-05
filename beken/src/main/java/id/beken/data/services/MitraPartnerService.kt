package id.beken.data.services

import androidx.lifecycle.ViewModelProvider
import id.beken.BuildConfig
import id.beken.models.MitraPartner
import id.beken.utils.helpers.retrofit
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface MitraPartnerService : ViewModelProvider.Factory {
    companion object {
        fun create(): MitraPartnerService {
            return retrofit(BuildConfig.BASE_URL).create(MitraPartnerService::class.java)
        }
    }

    @Headers("Content-Type: application/json")
    @POST("/api/mitra-partner/v1/kirim-tunai/mitra-register")
    suspend fun register(
        @Header("Authorization") signature: String,
        @Header("public-key") publicKey: String,
        @Body body: HashMap<String, String?>
    ): Response<MitraPartner>
}