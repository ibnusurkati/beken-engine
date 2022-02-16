package id.beken.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse (

    @SerialName("status")
    val status: Int? = null,

    @SerialName("message")
    val message: String? = null,

    @SerialName("code")
    val code: String? = null,

    @SerialName("data")
    val data: Data? = null,

    ){

    @Serializable
    data class Data(

        @SerialName("saldo_kurang")
        val saldoKurang: Int? = null,

        @SerialName("success")
        val success: Boolean? = null,
    )
}
