package id.beken.models

import kotlinx.serialization.Serializable

@Serializable
data class Contact (
    val name: String,
    val phoneNumber: String
)
