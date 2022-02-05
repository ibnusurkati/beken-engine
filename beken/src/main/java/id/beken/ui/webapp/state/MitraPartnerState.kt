package id.beken.ui.webapp.state

import id.beken.models.MitraPartner

sealed class MitraPartnerState {
    data class Error(val message: String?) : MitraPartnerState()
    data class Success(val body: MitraPartner?) : MitraPartnerState()
}