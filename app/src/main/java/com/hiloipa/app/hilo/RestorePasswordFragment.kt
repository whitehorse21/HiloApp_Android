package com.hiloipa.app.hilo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_restore_password.*


/**
 * A simple [Fragment] subclass.
 */
class RestorePasswordFragment : Fragment() {

    companion object {
        fun newInstance(): RestorePasswordFragment {
            val args = Bundle()
            val fragment = RestorePasswordFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_restore_password, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { activity.onBackPressed() }
    }
}
