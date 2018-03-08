/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.contacts

import android.app.DialogFragment
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.LocalBroadcastManager
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.ImportContactsRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.ImportContactsResponse
import com.hiloipa.app.hilo.models.responses.ImportedContact
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.displaySize
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_import_contacts.*
import java.io.ByteArrayOutputStream

/**
 * Created by Eduard Albu on 08 Март 2018.
 * For project: Hilo
 * Copyright (c) 2018. Fabity.co
 */
class ImportDialogFragment: DialogFragment() {

    lateinit var contacts: ArrayList<DeviceContact>

    companion object {
        const val actionContactsImported = "com.hiloipa.app.hilo.ui.contacts.CONTACTS_IMPORTED"

        fun newInstance(contacts: ArrayList<DeviceContact>): ImportDialogFragment {
            val args = Bundle()
            args.putParcelableArrayList("contacts", contacts)
            val fragment = ImportDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.alert_import_contacts, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contacts = arguments.getParcelableArrayList("contacts")
        importContacts()
    }

    override fun onResume() {
        super.onResume()
        val displaySize = activity.displaySize()
        val width = displaySize.first - 200
        val height = displaySize.second / 2
        dialog.window!!.setLayout(width, height)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun importContacts() {
        val request = ImportContactsRequest()
        request.contacts = contacts
        HiloApp.api().importContacts(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ImportContactsResponse> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            importedContactsLabel.text = getString(R.string.imported_contacts_d, data.imported)
                            val importedContacts = arrayListOf<Pair<String, Uri>>()
                            data.importedContacts.forEach { imported: ImportedContact ->
                                val deviceContact = contacts.firstOrNull { it.uuid == imported.uuid }
                                if (deviceContact != null && deviceContact.photoPath != null)
                                    importedContacts.add(Pair("${imported.contactId}", deviceContact.photoPath!!))
                            }
                            importContactImages(importedContacts)
                        }
                    } else {
                        activity.showExplanation(message = response.message)
                        this.dismiss()
                    }
                }, { error: Throwable ->
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                    this.dismiss()
                })
    }

    private fun importContactImages(contacts: ArrayList<Pair<String, Uri>>) {
        titleLabel.text = getString(R.string.uploading_contact_images)
        val observables = arrayListOf<Observable<HiloResponse<String>>>()
        importProgressBar.visibility = View.GONE
        photosProgressLayout.visibility = View.VISIBLE
        contacts.forEach { pair: Pair<String, Uri> ->
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity.contentResolver, pair.second)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                val request = StandardRequest()
                request.image = encodedImage
                request.contactId = pair.first
                observables.add(HiloApp.api().updateContanctImage(request))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        var progress = 0
        uploadedImagesLabel.text = getString(R.string.uploaded_images_d, progress)
        Observable.concat(observables)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    LocalBroadcastManager.getInstance(activity)
                            .sendBroadcast(Intent(actionContactsImported))
                    this.dismiss()
                }.doOnNext { response: HiloResponse<String> ->
                    if (response.status.isSuccess()) {
                        progress += 1
                        progressBar.setProgressWithAnimation(((progress.toFloat() / observables.size.toFloat()) * 100))
                        uploadedImagesLabel.text = getString(R.string.uploaded_images_d, progress)
                        progressLabel.text = "${progressBar.progress.toInt()}%"
                    }
                }.doOnError { error: Throwable ->
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                    LocalBroadcastManager.getInstance(activity)
                            .sendBroadcast(Intent(actionContactsImported))
                    this.dismiss()
                }.subscribe()
    }
}