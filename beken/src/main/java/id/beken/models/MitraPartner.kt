package id.beken.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MitraPartner(

    @SerialName("status")
    val status: Int? = null,

    @SerialName("message")
    val message: String? = null,

	@SerialName("code")
	val code: String? = null,

    @SerialName("data")
    val data: Data? = null,

    ) {

    @Serializable
    data class Data(

        @SerialName("id")
        val id: Int? = null,

        @SerialName("partner_id")
        val partnerId: Int? = null,

        @SerialName("uuid")
        val uuid: String? = null,

        @SerialName("name")
        val name: String? = null,

        @SerialName("phone_number")
        val phoneUmber: String? = null,

        @SerialName("email")
        val email: String? = null,

        @SerialName("balance")
        val balance: Int? = null
    )
}
