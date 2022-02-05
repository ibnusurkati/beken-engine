package id.beken.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionData (

    @SerialName("url")
    val url: String,

    @SerialName("status")
    val status: Boolean?,

    @SerialName("productName")
    val productName: String,

    @SerialName("data")
    val data: String
)