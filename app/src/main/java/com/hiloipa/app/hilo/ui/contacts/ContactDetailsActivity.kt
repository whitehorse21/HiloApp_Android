package com.hiloipa.app.hilo.ui.contacts

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.FullContactDetails
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.HelpActivity
import com.hiloipa.app.hilo.ui.more.email.EmailTemplatesFragment
import com.hiloipa.app.hilo.ui.reachout.ReachoutLogsFragment
import com.hiloipa.app.hilo.ui.widget.CameraActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contact_details.*
import vn.tungdx.mediapicker.MediaItem
import vn.tungdx.mediapicker.MediaOptions
import vn.tungdx.mediapicker.activities.MediaPickerActivity
import java.io.ByteArrayOutputStream
import java.io.File

val contactDetailsHelp = "Welcome to the Contact Detail view! Here you can edit and view all of the" +
        " information you have for a contact.\r\n\r\nUse the horizontal scroll menu to toggle " +
        "between the data tabs by clicking on the section you would like to use (i.e. Personal, " +
        "Reach Out Log, Notes, etc.).\r\n\r\nExpand the sections in the Personal tab to edit or " +
        "view each area (e.g. Contact Info, Tags & Custom Fields, etc.).\r\n\r\nYou can also send " +
        "Email Templates and Assign an Email Campaign to a contact by accessing those tabs in the " +
        "horizontal menu."
class ContactDetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    companion object {
        const val contactIdKey = "com.hiloipa.app.hilo.ui.contacts.CONTACT_ID"
    }

    private lateinit var contactDetails: FullContactDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
        tabLayout.setOnTabSelectedListener(this)

        if (intent.hasExtra(contactIdKey)) {
            val contactId = intent.extras.getString(contactIdKey)
            getFullContactDetails(contactId)
        } else {
            finish()
        }
    }

    private fun getFullContactDetails(contactId: String) {
        val request = StandardRequest()
        request.contactId = contactId

        val loading = showLoading()
        HiloApp.api().getContactFullDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FullContactDetails> ->
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            this.contactDetails = data
                            this.updateUIWithNewData(loading)
                        }
                    } else {
                        loading.dismiss()
                        showExplanation(message = response.message)
                    }
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun updateUIWithNewData(loading: AlertDialog = showLoading()) {
        // set basic data like name and avatar
        contactFullName.text = "${contactDetails.contactDetails.firstName} " +
                "${contactDetails.contactDetails.lastName}"
        Picasso.with(this)
                .load(contactDetails.contactDetails.userImage)
                .placeholder(R.mipmap.ic_profile_default_round)
                .error(R.mipmap.ic_profile_default_round)
                .into(contactImage)

        loading.dismiss()

        contactImage.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.alert_user_image, null)
            val image: ImageView = dialogView.findViewById(R.id.imageView)
            val button: RalewayButton = dialogView.findViewById(R.id.updateBtn)
            Picasso.with(this).load(contactDetails.contactDetails.userImage).into(image)
            val dialog = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .create()

            button.setOnClickListener {
                dialog.dismiss()
                showUpdateImageAlert()
            }

            dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.show()
        }
        replaceFragment(EditContactFragment.newInstance(contactId = "${contactDetails.contactDetails.contactId}"))
    }

    private fun showUpdateImageAlert() {
        val dialog = AlertDialog.Builder(this)
                .setTitle(getString(R.string.change_avatar))
                .setMessage(getString(R.string.change_avatar_message))
                .setPositiveButton(getString(R.string.from_gallery), { dialog, which ->
                    val options = MediaOptions.Builder()
                            .selectPhoto()
                            .build()
                    MediaPickerActivity.open(this, 1257, options)
                })
                .setNegativeButton(getString(R.string.from_camera), { dialog, which ->
                    val pictureIntent = Intent(this, CameraActivity::class.java)
                    this.startActivityForResult(pictureIntent, 1343)
                })
                .setNeutralButton(getString(R.string.cancel), null)
                .create()
        dialog.show()
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        transaction.commit()
    }

    override fun onTabReselected(tab: TabLayout.Tab) {}

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabSelected(tab: TabLayout.Tab) {
        val tabType = TabType.fromInt(tab.position)
        when (tabType) {
            TabType.personal -> replaceFragment(EditContactFragment
                    .newInstance(contactId = "${contactDetails.contactDetails.contactId}"))

            TabType.reach_out_logs -> replaceFragment(ReachoutLogsFragment
                    .newInstance(contactId = "${contactDetails.contactDetails.contactId}"))

            TabType.notes -> replaceFragment(ContactNotesFragment
                    .newInstance("${contactDetails.contactDetails.contactId}"))

            TabType.products -> replaceFragment(UserProductsFragment
                    .newInstace("${contactDetails.contactDetails.contactId}"))

            TabType.email_templates -> replaceFragment(EmailTemplatesFragment.newInstance())
        }
    }

    enum class TabType {
        personal, reach_out_logs, notes, products, documents, campaigns, email_templates;

        companion object {
            fun fromInt(int: Int): TabType = when (int) {
                0 -> personal
                1 -> reach_out_logs
                2 -> notes
                3 -> products
                4 -> documents
                5 -> campaigns
                else -> email_templates
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_help -> {
                val helpIntent = Intent(this, HelpActivity::class.java)
                helpIntent.putExtra(HelpActivity.titleKey, toolbarTitle.text)
                helpIntent.putExtra(HelpActivity.contentKey, contactDetailsHelp)
                startActivity(helpIntent)
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var image: Pair<String, String>? = null
            when(requestCode) {
                1257 -> {
                    val items: ArrayList<MediaItem>? = MediaPickerActivity.getMediaItemSelected(data);
                    if (items != null && items.size > 0) {
                        val mediaItem: MediaItem = items.get(0)
                        val path: String? = mediaItem.getPathOrigin(this)
                        if (path != null) {
                            val file = File(path)
                            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                            val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                            image = Pair(first = file.absolutePath, second = encodedImage)
                        }
                    }
                }

                1343 -> {
                    if (data != null) {
                        val file = File(data.extras.getString(CameraActivity.pathKey))
                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                        image = Pair(first = file.absolutePath, second = encodedImage)
                    }
                }
            }

            if (image != null) {
                this.saveUserImage(image)
            }
        }
        supportFragmentManager.fragments.forEach { it.onActivityResult(requestCode, resultCode, data) }
    }

    private fun saveUserImage(imageData: Pair<String, String>) {
        val request = StandardRequest()
        request.image = imageData.second
        request.contactId = "${contactDetails.contactDetails.contactId}"

        val loading = showLoading()
        HiloApp.api().updateContanctImage(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val file = File(imageData.first)
                        Picasso.with(this)
                                .load(file)
                                .into(contactImage)
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }
}
