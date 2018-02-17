package com.hiloipa.app.hilo.ui.todos

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TodosAdapter
import kotlinx.android.synthetic.main.alert_todos.*

/**
 * Created by eduardalbu on 17.02.2018.
 */
class TodoDetailsFragment(): BottomSheetDialogFragment(), TodosAdapter.TodoDelegate {

    lateinit var adapter: TodosAdapter

    companion object {
        fun newInstance(title: String, showCheckBox: Boolean = true): TodoDetailsFragment {
            val args = Bundle()
            args.putString("title", title)
            args.putBoolean("showCheckBox", showCheckBox)
            val fragment = TodoDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.alert_todos, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments.getString("title")
        backButton.text = title

        backButton.setOnClickListener { this.dismiss() }

        adapter = TodosAdapter(context, showCheckBox = arguments.getBoolean("showCheckBox"))
        adapter.delegate = this
        // setup recycler view
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        addBtn.setOnClickListener { onTodoEditClicked() }
    }

    override fun onItemClicked() {

    }

    override fun onTodoCheckedChanged(isChecked: Boolean) {

    }

    override fun onTodoEditClicked() {
        val editIntent = Intent(activity, CreateTodoActivity::class.java)
        val extras = Bundle()
        extras.putBoolean("is event", !arguments.getBoolean("showCheckBox"))
        editIntent.putExtras(extras)
        activity.startActivity(editIntent)
    }
}