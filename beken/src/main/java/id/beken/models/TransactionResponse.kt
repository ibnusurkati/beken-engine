package id.beken.models

data class TransactionResponse (
    val status: Int? = null,
    val message: String? = null,
    val code: String? = null,
    val data: Data? = null,
    ){
    data class Data(
        val saldoKurang: Int? = null,
        val success: Boolean? = null,
    )
}
