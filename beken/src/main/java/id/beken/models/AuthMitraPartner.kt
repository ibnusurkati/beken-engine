package id.beken.models

import java.io.Serializable

data class AuthMitraPartner(
    val uuid: String,
    val name: String?,
    val email: String,
    val phoneNumber: String?,
    val secretKey: String,
    val publicKey: String,
    val debug: Boolean = true
) : Serializable
