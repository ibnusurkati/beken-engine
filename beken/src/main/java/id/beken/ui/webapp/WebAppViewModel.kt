package id.beken.ui.webapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.beken.data.services.MitraPartnerService
import id.beken.models.AuthMitraPartner
import id.beken.models.MitraPartner
import id.beken.ui.webapp.state.MitraPartnerState
import id.beken.utils.extensions.sha256
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Suppress("BlockingMethodInNonBlockingContext")
class WebAppViewModel: ViewModel() {

    val mitraPartnerState = MutableLiveData<MitraPartnerState>()
    val isLoading = MutableLiveData(true)

    fun registerMitraPartner(authMitraPartner: AuthMitraPartner) {
        val signature = (authMitraPartner.publicKey + authMitraPartner.secretKey).sha256()
        val payload = HashMap<String, String?>()

        payload["uuid"] = authMitraPartner.uuid
        payload["name"] = authMitraPartner.name
        payload["phoneNumber"] = authMitraPartner.phoneNumber
        payload["email"] = authMitraPartner.email

        viewModelScope.launch {
            kotlin.runCatching {
                MitraPartnerService.create().register(signature, authMitraPartner.publicKey, payload)
            }.onSuccess {
                if (it.isSuccessful) {
                    mitraPartnerState.value = MitraPartnerState.Success(it.body())
                } else {
                    val errorBody = it.errorBody()
                    if (errorBody != null) {
                        val mitraPartner = Json.decodeFromString<MitraPartner>(errorBody.string())
                        mitraPartnerState.value = MitraPartnerState.Error(mitraPartner.code)
                    } else {
                        mitraPartnerState.value = MitraPartnerState.Error(null)
                    }

                }
            }.onFailure {
                it.printStackTrace()
                mitraPartnerState.value = MitraPartnerState.Error(it.message)
            }
        }
    }

    fun setLoading(status: Boolean) {
        isLoading.value = status
    }
}