package com.hiloipa.app.hilo.ui.todos

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TodosAdapter
import com.hiloipa.app.hilo.models.requests.CompleteGoalRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.alert_todos.*
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * Created by eduardalbu on 17.02.2018.
 */
class TodoDetailsFragment<T: ToDo>(): BottomSheetDialogFragment(), TodosAdapter.TodoDelegate {

    lateinit var adapter: TodosAdapter<T>
    lateinit var todoType: TodoType
    var data: ArrayList<T> = arrayListOf()

    companion object {
        fun <T: ToDo> newInstance(title: String, type: TodoType, data: ArrayList<T>): TodoDetailsFragment<T> {
            val args = Bundle()
            args.putString("title", title)
            args.putInt("type", type.toInt())
            args.putParcelableArrayList("data", data)
            val fragment = TodoDetailsFragment<T>()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.alert_todos, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filter = IntentFilter(CreateTodoActivity.actionUpdateDashboard)
        LocalBroadcastManager.getInstance(activity).registerReceiver(broadcastReceiver, filter)

        val title = arguments.getString("title")
        backButton.text = title

        backButton.setOnClickListener { this.dismiss() }

        this.data.addAll(arguments.getParcelableArrayList("data"))
        this.todoType = TodoType.fromInt(arguments.getInt("type"))

        adapter = TodosAdapter(context, todoType, data)
        adapter.delegate = this
        // setup recycler view
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        addBtn.setOnClickListener {
            val editIntent = Intent(activity, CreateTodoActivity::class.java)
            val extras = Bundle()
            extras.putInt("type", todoType.toInt())
            editIntent.putExtras(extras)
            activity.startActivityForResult(editIntent, 2155)
        }
    }

    override fun onItemClicked(toDo: ToDo, position: Int) {
        showToDoDetails(toDo)
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) return
            when(intent.action) {
                CreateTodoActivity.actionUpdateDashboard -> this@TodoDetailsFragment.dismiss()
            }
        }
    }

    override fun onTodoCheckedChanged(toDo: ToDo, position: Int) {
        if (toDo.isSelected) {
            val alert = AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.hilo))
                    .setMessage(getString(R.string.confirm_goal_complete, todoType.title()))
                    .setPositiveButton(getString(R.string.yes), { dialog, which ->
                        dialog.dismiss()
                        val loading = activity.showLoading()
                        val request = CompleteGoalRequest()
                        request.targetId = "${toDo.id}"
                        request.targetType = todoType.apiName()
                        HiloApp.api().completeGoal(request)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ response: ResponseBody ->
                                    loading.dismiss()
                                    val body = JSONObject(response.string())
                                    if (body.getInt("Status").isSuccess()) {
                                        LocalBroadcastManager.getInstance(activity)
                                                .sendBroadcast(Intent(CreateTodoActivity.actionUpdateDashboard))
                                        adapter.data.removeAt(position)
                                        adapter.notifyItemRemoved(position)
                                    } else activity.showExplanation(message = body.getString("Message"))
                                }, { error: Throwable ->
                                    loading.dismiss()
                                    error.printStackTrace()
                                    activity.showExplanation(message = error.localizedMessage)
                                })
                    })
                    .setNegativeButton(getString(R.string.no), { dialog, which ->
                        toDo.isSelected = false
                        adapter.notifyItemChanged(position)
                    })
                    .create()
            alert.show()
        }
    }

    override fun onTodoEditClicked(toDo: ToDo, position: Int) {
        showToDoDetails(toDo)
    }

    private fun showToDoDetails(toDo: ToDo) {
        val editIntent = Intent(activity, CreateTodoActivity::class.java)
        val extras = Bundle()
        extras.putInt("type", todoType.toInt())

        when(todoType) {
            TodoType.goal -> extras.putParcelable("data", toDo as Goal)
            TodoType.action -> extras.putParcelable("data", toDo as Action)
            TodoType.need -> extras.putParcelable("data", toDo as TeamNeed)
            TodoType.event -> extras.putParcelable("data", toDo as Event)
        }

        editIntent.putExtras(extras)
        activity.startActivity(editIntent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(broadcastReceiver)
    }
}