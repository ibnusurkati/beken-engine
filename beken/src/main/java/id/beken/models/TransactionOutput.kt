package id.beken.models

import kotlinx.serialization.Serializable

@Serializable
data class TransactionOutput (
    val prefixFilename: String,
    val content: String
)