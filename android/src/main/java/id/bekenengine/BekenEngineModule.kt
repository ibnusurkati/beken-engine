package id.bekenengine

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import id.beken.BekenApp
import id.beken.models.AuthMitraPartner
import org.json.JSONObject

class BekenEngineModule(
    private val reactContext: ReactApplicationContext,
) : ReactContextBaseJavaModule(reactContext) {

    init {
        BekenApp.observerPaymentOnBackground(BekenApp.FROM_NATIVE) {
            val data = Arguments.createMap()
            data.putString("data", it.data)
            emit("listener::${it.productName.lowercase()}", data)
        }
    }

    override fun getName(): String = "BekenEngine"

    fun emit(eventName: String, params: WritableMap) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    @ReactMethod
    fun push(eventName: String, status: Boolean, data: String) {
        val eventNameSplit = eventName.split("::").toTypedArray()
        val productName = eventNameSplit[1].uppercase()
        BekenApp.push(status, productName, data)
    }

    @ReactMethod
    fun open(params: String) {
        val auth = JSONObject(params)
        val authMitraPartner = AuthMitraPartner(
            uuid = auth.getString("uuid"),
            name = auth.optString("name"),
            email = auth.getString("email"),
            phoneNumber = auth.optString("phoneNumber"),
            publicKey = auth.getString("publicKey"),
            secretKey = auth.getString("secretKey")
        )

        BekenApp.open(reactContext, authMitraPartner)
    }

}
