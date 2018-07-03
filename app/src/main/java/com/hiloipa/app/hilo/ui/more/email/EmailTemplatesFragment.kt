package com.hiloipa.app.hilo.ui.more.email


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.EmailTemplatesAdapter
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.Template
import com.hiloipa.app.hilo.ui.base.BaseFragment
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_email_templates.*


/**
 * A simple [Fragment] subclass.
 */
class EmailTemplatesFragment : BaseFragment(), TabLayout.OnTabSelectedListener, EmailTemplatesAdapter.EmailTemplateDelegate {

    lateinit var adapter: EmailTemplatesAdapter
    private val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"


    companion object {
        fun newInstance(): EmailTemplatesFragment {
            val args = Bundle()
            val fragment = EmailTemplatesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_email_templates, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.setOnTabSelectedListener(this)
        adapter = EmailTemplatesAdapter(activity!!)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false

        getTemplates()
    }

    private fun getTemplates() {
        val loading = activity!!.showLoading()
        HiloApp.api().getTemplates(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ArrayList<Template>> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            val templates = arrayListOf<Template>()
                            if (tabLayout.selectedTabPosition != 1 && tabLayout.selectedTabPosition != 2)
                                templates.addAll(data)
                            else if (tabLayout.selectedTabPosition == 1) {
                                data.forEach {
                                    if (it.type == 0)
                                        templates.add(it)
                                }
                            } else if (tabLayout.selectedTabPosition == 2) {
                                data.forEach {
                                    if (it.type == 1)
                                        templates.add(it)
                                }
                            }

                            adapter.refreshList(templates)
                        }
                    } else activity!!.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity!!.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onPreviewTemplateClicked(template: Template, position: Int) {
        activity!!.openUrl(template.previewLink)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        getTemplates()
    }
}
