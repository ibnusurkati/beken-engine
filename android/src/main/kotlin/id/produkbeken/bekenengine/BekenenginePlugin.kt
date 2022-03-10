package id.produkbeken.bekenengine

import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import id.beken.BekenApp
import id.beken.models.AuthMitraPartner

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import org.json.JSONObject
import java.lang.Exception

/** BekenenginePlugin */
class BekenenginePlugin : FlutterPlugin, MethodCallHandler, StreamHandler {

    private lateinit var context: Context
    private lateinit var eventChannel: EventChannel
    private lateinit var methodChannel: MethodChannel

    init {
        BekenApp.observerPaymentOnBackground(BekenApp.FROM_NATIVE) {
            val data = HashMap<String, Any>()
            data.put("data", it.data)
            Log.d("DATA TRX", data.toString())
//            emit("listener::${it.productName.lowercase()}", data)
        }
    }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        methodChannel = MethodChannel(flutterPluginBinding.binaryMessenger, "method_bekenengine")
        methodChannel.setMethodCallHandler(this)

        eventChannel = EventChannel(flutterPluginBinding.binaryMessenger, "event_bekenengine")
        eventChannel.setStreamHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        try {
            Log.d("method", call.method)
            when(call.method) {
                "open" -> {
                    val auth = JSONObject(call.arguments.toString())
                    val authMitraPartner = AuthMitraPartner(
                        uuid = auth.getString("uuid"),
                        name = auth.optString("name"),
                        email = auth.getString("email"),
                        phoneNumber = auth.optString("phoneNumber"),
                        publicKey = auth.getString("publicKey"),
                        secretKey = auth.getString("secretKey"),
                        debug = auth.optBoolean("debug")
                    )
                    BekenApp.open(
                        context = context,
                        authMitraPartner
                    )
                }
                else -> {
                    result.notImplemented()
                }
            }
        } catch (e: Exception) {
            result.error("ERROR METHOD CHANNEL", e.message, e.stackTrace)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventSink?) {
        Log.d("EVENT FLUTTER", "RUN")
        Log.d("EVENT DATA", arguments.toString())
    }

    override fun onCancel(arguments: Any?) {
        Log.d("EVENT FLUTTER", "STOP")
        Log.d("EVENT DATA", arguments.toString())
    }
}
