package id.beken.ui.webapp.state

sealed class TransactionState {
    data class Error(val message: String?) : TransactionState()
    data class Success(val body: String?) : TransactionState()
}