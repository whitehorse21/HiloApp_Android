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
import com.hiloipa.app.hilo.utils.openUrl
import kotlinx.android.synthetic.main.fragment_email_templates.*


/**
 * A simple [Fragment] subclass.
 */
class EmailTemplatesFragment : Fragment(), TabLayout.OnTabSelectedListener, EmailTemplatesAdapter.EmailTemplateDelegate {

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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_email_templates, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout.setOnTabSelectedListener(this)
        adapter = EmailTemplatesAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
    }

    override fun onPreviewTemplateClicked() {
        activity.openUrl("http://fabity.co/")
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        val templatesType = TemplatesType.fromInt(tab.position)
        when(templatesType) {
            TemplatesType.all -> {

            }

            TemplatesType.my -> {

            }

            TemplatesType.shared_downline -> {

            }

            TemplatesType.shared_upline -> {

            }

            TemplatesType.hilo -> {

            }
        }
    }

    enum class TemplatesType {
        all, my, shared_downline, shared_upline, hilo;

        companion object {
            fun fromInt(int: Int): TemplatesType = when(int) {
                1 -> my
                2 -> shared_downline
                3 -> shared_upline
                4 -> hilo
                else -> all
            }
        }
    }
}
