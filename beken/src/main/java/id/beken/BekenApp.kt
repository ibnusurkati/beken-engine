package id.beken

import android.content.Context
import android.content.Intent
import id.beken.models.AuthMitraPartner
import id.beken.models.TransactionData
import id.beken.ui.webapp.WebAppActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

class BekenApp {
    companion object {

        const val FROM_NATIVE = "NATIVE"
        const val FROM_PARTNER = "PARTNER"
        private const val EXTRA_NAME = "MITRA"

        private val compositeDisposable = CompositeDisposable()
        private val sStream: Subject<TransactionData> =
            PublishSubject.create<TransactionData>().toSerialized()

        fun open(context: Context, authMitraPartner: AuthMitraPartner) {
            val intent = Intent(context, WebAppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(EXTRA_NAME, authMitraPartner)
            context.startActivity(intent)
        }

        @JvmStatic
        fun push(status: Boolean, productName: String, data: String) {
            sStream.onNext(TransactionData(FROM_PARTNER, status, productName, data))
        }

        @JvmStatic
        fun pushFrom(from: String, productName: String, data: String) {
            sStream.onNext(TransactionData(from, null, productName, data))
        }

        @JvmStatic
        fun observerPaymentOnUI(from: String, action: (eventPayment: TransactionData) -> Unit) {
            val observable = sStream.filter { it.url == from }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            compositeDisposable.add(observable.subscribe {
                action.invoke(it)
            })
        }

        @JvmStatic
        fun observerPaymentOnBackground(from: String, action: (eventPayment: TransactionData) -> Unit) {
            val observable = sStream.filter { it.url == from }.subscribeOn(Schedulers.io())
            compositeDisposable.add(observable.subscribe {
                action.invoke(it)
            })
        }
    }
}