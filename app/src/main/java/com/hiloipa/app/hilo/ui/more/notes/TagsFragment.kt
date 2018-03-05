package com.hiloipa.app.hilo.ui.more.notes


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.SelectTagsAdapter
import com.hiloipa.app.hilo.models.requests.DeleteTagRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.models.responses.NoteTag
import com.hiloipa.app.hilo.models.responses.NoteTagsResponse
import com.hiloipa.app.hilo.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tags.*


/**
 * A simple [Fragment] subclass.
 */
class TagsFragment : BottomSheetDialogFragment(), SelectTagsAdapter.SelectTagDelegate {

    lateinit var adapter: SelectTagsAdapter

    var selectedTags: ArrayList<NoteTag> = arrayListOf()

    companion object {
        const val actionTagsSelected = "com.hiloipa.app.hilo.ui.more.notes.TAGS_SELECTED"
        fun newInstance(selectedTags: ArrayList<NoteTag>): TagsFragment {
            val args = Bundle()
            args.putParcelableArrayList("tags", selectedTags)
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

        selectedTags.addAll(arguments.getParcelableArrayList("tags"))

        addTagBtn.setOnClickListener { addNewTag() }

        getTagsList()
    }

    private fun addNewTag() {
        val tagName = newTagField.text.toString()
        if (tagName.isEmpty()) {
            Toast.makeText(activity, getString(R.string.enter_tag_name),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val loading = activity.showLoading()
        val request = DeleteTagRequest()
        request.tagName = tagName
        HiloApp.api().addNoteTag(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        getTagsList()
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                        newTagField.setText("")
                        activity.hideKeyboard()
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
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
                        if (data != null) {
                            val tags = data.tags
                            tags.forEach { it.isSelected = selectedTags.contains(it) }
                            adapter.refreshData(tags)
                        }
                    } else activity.showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    activity.showExplanation(message = error.localizedMessage)
                })
    }

    override fun onRemoveTagClicked(tag: NoteTag, position: Int) {
        val alert = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.delete))
                .setMessage(getString(R.string.confirm_delete_tag))
                .setPositiveButton(getString(R.string.yes), { dialog, which ->
                    dialog.dismiss()
                    val loading = activity.showLoading()
                    val request = DeleteTagRequest()
                    request.tagName = tag.name
                    HiloApp.api().deleteTag(request)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({ response: HiloResponse<String> ->
                                loading.dismiss()
                                if (response.status.isSuccess()) {
                                    adapter.tags.remove(tag)
                                    adapter.notifyItemRemoved(position)
                                } else activity.showExplanation(message = response.message)
                            }, { error: Throwable ->
                                loading.dismiss()
                                error.printStackTrace()
                                activity.showExplanation(message = error.localizedMessage)
                            })
                })
                .setNegativeButton(getString(R.string.no), null)
                .create()
        alert.show()
    }

    override fun onTagSelectedChanged(tag: NoteTag, position: Int) {
        if (tag.isSelected && !selectedTags.contains(tag))
            selectedTags.add(tag)
        else selectedTags.remove(tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val tagsIntent = Intent(actionTagsSelected)
        tagsIntent.putExtra("tags", selectedTags)
        LocalBroadcastManager.getInstance(activity)
                .sendBroadcast(tagsIntent)
    }
}
