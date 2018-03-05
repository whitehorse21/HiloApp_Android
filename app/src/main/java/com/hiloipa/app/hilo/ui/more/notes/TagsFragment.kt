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
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.NoteTag
import com.hiloipa.app.hilo.models.responses.NoteTagsResponse
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tags.*


/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : BottomSheetDialogFragment(), SelectTagsAdapter.SelectTagDelegate {

    lateinit var adapter: SelectTagsAdapter

    var selectedTags: MutableList<NoteTag> = mutableListOf()

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

        getTagsList()
    }

    private fun getTagsList() {
        val loading = activity.showLoading()
        HiloApp.api().getTagsList(StandardRequest())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<NoteTagsResponse> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            adapter.refreshData(data.tags)
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onRemoveTagClicked(tag: NoteTag, position: Int) {

    }

    override fun onTagSelectedChanged(tag: NoteTag, position: Int) {
        if (tag.isSelected && !selectedTags.contains(tag))
            selectedTags.add(tag)
        else selectedTags.remove(tag)
    }
}
