package id.beken.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import id.beken.models.Contact

class ContactRepository(
    private val context: Context
) {

    @SuppressLint("Range")
    fun findAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if ((cursor?.count ?: 0) > 0) {
            while (cursor!!.moveToNext()) {
                val name = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                )
                val phoneNumber = cursor.getString(
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                )
                contacts.add(Contact(name, phoneNumber))
            }
        }

        cursor?.close()

        return contacts
    }

    @SuppressLint("Range")
    fun findWithLimitOffset(limit: String, offset: String): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC LIMIT " + limit + " OFFSET " + offset
        )

        if ((cursor?.count ?: 0) > 0) {
            while (cursor!!.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(Contact(name, phoneNumber))
            }
        }

        cursor?.close()

        return contacts
    }

}