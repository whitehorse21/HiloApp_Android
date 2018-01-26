package com.hiloipa.app.hilo.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R


/**
 * A simple [Fragment] subclass.
 */
class EditContactFragment : Fragment() {

    companion object {
        fun newInstance(): EditContactFragment {
            val args = Bundle()
            val fragment = EditContactFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater!!.inflate(R.layout.fragment_edit_contact, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
