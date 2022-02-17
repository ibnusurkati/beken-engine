package id.beken.models

data class MitraPartner(
    val status: Int? = null,
    val message: String? = null,
	val code: String? = null,
    val data: Data? = null,
    ) {
    data class Data(
        val id: Int? = null,
        val partnerId: Int? = null,
        val uuid: String? = null,
        val name: String? = null,
        val phoneUmber: String? = null,
        val email: String? = null,
        val balance: Int? = null
    )
}
