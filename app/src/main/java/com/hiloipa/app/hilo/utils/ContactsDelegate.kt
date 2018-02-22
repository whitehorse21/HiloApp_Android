package com.hiloipa.app.hilo.utils

import com.hiloipa.app.hilo.models.responses.DetailedContact

/**
 * Created by eduardalbu on 02.02.2018.
 */
interface ContactsDelegate {
    fun onContactClicked(contact: DetailedContact, position: Int)
    fun onEditContactClicked(contact: DetailedContact, position: Int)
    fun onDeleteContactClicked(contact: DetailedContact, position: Int)
    fun onSendSmsClicked(contact: DetailedContact, position: Int)
}