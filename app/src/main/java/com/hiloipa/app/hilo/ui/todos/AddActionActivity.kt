/*
 * Copyright (c) 2018. Fabity.co / Developer: Eduard Albu
 */

package com.hiloipa.app.hilo.ui.todos

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.FiltersAdapter
import com.hiloipa.app.hilo.models.requests.ActionForGoalRequest
import com.hiloipa.app.hilo.models.requests.ActionToGoalRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.FilterValue
import com.hiloipa.app.hilo.models.responses.HiloResponse
import com.hiloipa.app.hilo.ui.base.BaseActivity
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_action.*
import kotlinx.android.synthetic.main.layout_todo_time.*
import java.text.SimpleDateFormat
import java.util.*

class AddActionActivity : BaseActivity(), FiltersAdapter.FiltersDelegate {

    lateinit var contactId: String
    lateinit var adapter: FiltersAdapter
    lateinit var goalId: String
    var selectedActions: MutableList<FilterValue> = mutableListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    val sessionFormat = SimpleDateFormat("aaa", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_action)

        toolbar.setNavigationOnClickListener { finish() }

        setupDueDateBtn()
        setupEndTimeBtn()
        setupAmPmSpinner()
        setupPrioritySpinner()

        existentActionToggleBtn.setOnClickListener {
            existentActionLayout.visibility = if (existentActionLayout.visibility == View.VISIBLE)
                View.GONE else View.VISIBLE
            existentActionToggleBtn.isSelected = existentActionLayout.visibility == View.VISIBLE
        }

        newActionToggleBtn.setOnClickListener {
            newActionLayout.visibility = if (newActionLayout.visibility == View.VISIBLE)
                View.GONE else View.VISIBLE
            newActionToggleBtn.isSelected = newActionLayout.visibility == View.VISIBLE
        }

        createActionBtn.setOnClickListener {
            addActionForGoal()
        }

        contactId = intent.extras.getString("contactId")
        goalId = intent.extras.getString("goalId")
        adapter = FiltersAdapter(arrayListOf())
        adapter.delegate = this
        // setup recycler view
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false

        getContactActions()

        assignActionBtn.setOnClickListener {
            addActionsToGoal()
        }

        doneBtn.setOnClickListener {
            addActionsToGoal()
        }
    }

    private fun setupActionsSpinner(contactActions: ArrayList<ActionItem>) {
        val actions = arrayListOf<FilterValue>()
        contactActions.forEach {
            val tag = FilterValue(it.name, "${it.id}")
            tag.isSelected = it.isSelected
            actions.add(tag)
        }
        adapter.refreshList(actions)
    }

    private fun getContactActions() {
        val loading = showLoading()
        val request = StandardRequest()
        request.contactId = this.contactId
        HiloApp.api().getContactActions(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ActionDropDown> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            setupActionsSpinner(data.actionItems)
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })

    }

    private fun addActionForGoal() {
        val title = actionTitleField.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_action_title),
                    Toast.LENGTH_SHORT).show()
            return
        }
        val dueDate = dueDateBtn.text.toString()
        val allDay = allDayCheckBox.isChecked
        val details = actionDescriptionField.text.toString()
        val priority = priorityBtn.text.toString()
        val time = endTimeBtn.text.toString()
        val session = amPmBtn.text.toString()

        val request = ActionForGoalRequest()
        request.allDay = if (allDay) "true" else "false"

        if (details.isNotEmpty())
            request.details = details
        if (priority.isNotEmpty())
            request.priority = priority
        if (time.isNotEmpty())
            request.time = time
        if (session.isNotEmpty())
            request.session = session
        if (dueDate.isNotEmpty())
            request.dueDate = dueDate

        request.title = title

        val loading = showLoading()
        HiloApp.api().addActionForGoal(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<Int> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            selectedActions.add(FilterValue(title, "$data"))
                        showExplanation(title = getString(R.string.hilo), message = response.message)
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun addActionsToGoal() {
        if (selectedActions.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_actions),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val builder = StringBuilder()
        selectedActions.forEach { builder.append(it.value).append(",") }
        val actions = ",$".toRegex().replace(builder.toString(), "")
        val request = ActionToGoalRequest()
        request.actions = actions
        request.goalId = goalId

        val loading = showLoading()
        HiloApp.api().addActionToGoal(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun setupDueDateBtn() {
        val calendar = Calendar.getInstance()
        dueDateBtn.setOnClickListener {
            val alert = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                dueDateBtn.text = dateFormat.format(newDate)
                dueDateBtn.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            alert.show()
        }
    }

    private fun setupEndTimeBtn() {
        val calendar = Calendar.getInstance()
        endTimeBtn.setOnClickListener {
            val alert = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newDate = Date(calendar.timeInMillis)
                endTimeBtn.text = timeFormat.format(newDate)
                endTimeBtn.tag = newDate
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            alert.show()
        }
    }

    private fun setupAmPmSpinner() {
        var isFromUser = false
        val sessions = mutableListOf<String>("AM", "PM")
        amPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        amPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    amPmBtn.text = sessions[position]
                    amPmBtn.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        amPmBtn.setOnClickListener {
            isFromUser = true
            amPmSpinner.performClick()
        }
    }

    private fun setupPrioritySpinner() {
        var isFromUser = false
        val priorities = mutableListOf<String>("", "Low", "Medium", "High")
        prioritySpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, priorities)
        prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    priorityBtn.text = priorities[position]
                    priorityBtn.tag = "${position}"
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        priorityBtn.setOnClickListener {
            isFromUser = true
            prioritySpinner.performClick()
        }
    }

    override fun didClickOnTag(value: FilterValue, position: Int) {
        if (selectedActions.contains(value) && !value.isSelected)
            selectedActions.remove(value)
        else if (value.isSelected)
            selectedActions.add(value)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(Intent(CreateTodoActivity.actionUpdateDashboard))
    }
}
