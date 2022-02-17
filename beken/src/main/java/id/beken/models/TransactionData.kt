package id.beken.models

data class TransactionData (
    val url: String,
    val status: Boolean?,
    val productName: String,
    val data: String
)