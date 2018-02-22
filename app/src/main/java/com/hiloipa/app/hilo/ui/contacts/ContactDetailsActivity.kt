package com.hiloipa.app.hilo.ui.contacts

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.FullContactDetails
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.more.email.EmailTemplatesFragment
import com.hiloipa.app.hilo.ui.reachout.ReachoutLogsFragment
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_contact_details.*

class ContactDetailsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    companion object {
        const val contactIdKey = "com.hiloipa.app.hilo.ui.contacts.CONTACT_ID"
    }

    lateinit var contactDetails: FullContactDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)
        toolbar.setNavigationOnClickListener { finish() }
        replaceFragment(EditContactFragment.newInstance())
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
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            this.contactDetails = data
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, fragment.javaClass.name)
        transaction.commit()
    }

    override fun onTabReselected(tab: TabLayout.Tab) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val tabType = TabType.fromInt(tab.position)
        when (tabType) {
            TabType.personal -> replaceFragment(EditContactFragment.newInstance())
            TabType.reach_out_logs -> replaceFragment(ReachoutLogsFragment.newInstance(isChild = true))
            TabType.notes -> replaceFragment(ContactNotesFragment.newInstance())
            TabType.products -> replaceFragment(UserProductsFragment.newInstace())

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
}
