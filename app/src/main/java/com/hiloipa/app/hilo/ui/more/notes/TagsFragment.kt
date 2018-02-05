package com.hiloipa.app.hilo.ui.more.notes


import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.SelectTagsAdapter
import kotlinx.android.synthetic.main.fragment_tags.*


/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : BottomSheetDialogFragment(), SelectTagsAdapter.SelectTagDelegate {

    lateinit var adapter: SelectTagsAdapter

    companion object {
        fun newInstance(): TagsFragment {
            val args = Bundle()
            val fragment = TagsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_tags, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton.setOnClickListener { this.dismiss() }
        adapter = SelectTagsAdapter(activity)
        adapter.delegate = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    override fun onRemoveTagClicked() {

    }

    override fun onTagSelectedChanged(isSelected: Boolean) {

    }
}
