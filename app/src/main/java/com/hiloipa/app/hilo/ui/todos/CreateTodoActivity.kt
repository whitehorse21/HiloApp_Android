package com.hiloipa.app.hilo.ui.todos

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.TagsSpinnerAdapter
import com.hiloipa.app.hilo.models.Tag
import com.hiloipa.app.hilo.models.requests.ActionForGoalRequest
import com.hiloipa.app.hilo.models.requests.ActionToGoalRequest
import com.hiloipa.app.hilo.models.requests.SaveEventRequest
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.FragmentSearchContacts
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_create_todo.*
import kotlinx.android.synthetic.main.layout_event_time.*
import kotlinx.android.synthetic.main.layout_todo_time.*
import java.text.SimpleDateFormat
import java.util.*

class CreateTodoActivity : AppCompatActivity(), FragmentSearchContacts.SearchDelegate {

    companion object {
        const val actionUpdateDashboard = "com.hiloipa.app.hilo.ui.todos.UPDATE_DASHBOARD"
    }

    var item: ToDo? = null
    lateinit var todoType: TodoType
    var actions: ArrayList<ActionItem> = arrayListOf()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("hh:mm", Locale.getDefault())
    val sessionFormat = SimpleDateFormat("aaa", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)
        toolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        // get to do type from intent
        todoType = TodoType.fromInt(intent.extras.getInt("type"))

        // setup spinners and date buttons
        setupAmPmSpinner()
        setupPrioritySpinner()
        setupDueDateBtn()
        setupEndTimeBtn()
        setupEventAmPmSpinner()
        setupEventTypeSpinner()
        setupStartAndEndDateBtns()

        // get item from intent
        when (todoType) {
            TodoType.goal -> item = intent.extras.getParcelable("data") as Goal?
            TodoType.action -> item = intent.extras.getParcelable("data") as Action?
            TodoType.need -> item = intent.extras.getParcelable("data") as TeamNeed?
            TodoType.event -> item = intent.extras.getParcelable("data") as Event?
        }

        if (todoType == TodoType.goal)
            getContactActions()

        todoTimeLayout.visibility = if (todoType == TodoType.event) View.GONE else View.VISIBLE
        eventTimeLayout.visibility = if (todoType == TodoType.event) View.VISIBLE else View.GONE
        actionsLayout.visibility = if (todoType == TodoType.goal) View.VISIBLE else View.GONE
        locationLabel.visibility = if (todoType == TodoType.event) View.VISIBLE else View.GONE
        assignActionBtn.visibility = if (todoType == TodoType.goal && item == null) View.VISIBLE else View.GONE

        todoTitleField.hint = getString(R.string.s_title, todoType.title())
        toolbarTitle.text = todoType.title()
        allDayCheckBox.text = getString(R.string.all_day_s, todoType.title())
        headerButton.text = getString(R.string.add_s, todoType.title())

        contactField.setOnClickListener {
            FragmentSearchContacts.newInstance(this)
                    .show(fragmentManager, "SearchContacts")
        }

        assignActionBtn.setOnClickListener {
            if (todoType == TodoType.goal) {
                addGoal(goToActions = true)
            }
        }

        if (item != null) {
            headerButton.text = getString(R.string.edit_s, todoType.title())
            todoTitleField.setText(item!!.name)
            allDayCheckBox.isChecked = item!!.allDay ?: false
            contactField.text = item!!.contactName
            contactField.tag = "${item!!.contactId}"
            todoDescriptionField.setText(item!!.details)


            when (todoType) {
                TodoType.goal -> {
                    val dueDate = item!!.dueDate
                    priorityBtn.text = item!!.priority
                    priorityBtn.tag = "${item!!.priorityId}"
                    if (dueDate != null) {
                        dueDateBtn.text = dateFormat.format(dueDate)
                        endTimeBtn.text = timeFormat.format(dueDate)
                        amPmBtn.text = sessionFormat.format(dueDate)
                    }
                    val actions = (item as Goal).actions
                    val builder = StringBuilder()
                    val regex = "(,$)*".toRegex()
                    actions.forEach { builder.append(it.name).append(",") }
                    actionsBtn.text = regex.replace(builder.toString(), "")
                    builder.setLength(0)
                    actions.forEach { builder.append(it.id).append(",") }
                    actionsBtn.tag = regex.replace(builder.toString(), "")
                    builder.setLength(0)
                }
                TodoType.action, TodoType.need -> {
                    val dueDate = item!!.dueDate
                    priorityBtn.text = item!!.priority
                    priorityBtn.tag = "${item!!.priorityId}"
                    if (dueDate != null) {
                        dueDateBtn.text = dateFormat.format(dueDate)
                        endTimeBtn.text = timeFormat.format(dueDate)
                        amPmBtn.text = sessionFormat.format(dueDate)
                    }
                }
                TodoType.event -> {
                    val event = item as Event
                    // setup start date
                    eventStartDate.text = dateFormat.format(event.startDate)
                    eventStartTime.text = timeFormat.format(event.startDate)
                    eventStartTimeAmPm.text = sessionFormat.format(event.startDate)

                    // setup end date
                    eventEndDate.text = dateFormat.format(event.endDate)
                    eventEndTime.text = timeFormat.format(event.endDate)
                    eventEndTimeAmPm.text = sessionFormat.format(event.endDate)

                    // location
                    locationLabel.setText(event.location)

                    // event type
                    eventTypeBtn.text = event.eventType
                }
            }
        }

        saveBtn.setOnClickListener {
            if (todoType != TodoType.event)
                addGoal()
            else saveEvent()
        }
    }

    override fun onContactSelected(contact: Contact) {
        contactField.text = contact.name
        contactField.tag = "${contact.id}"
    }

    private fun setupDueDateBtn() {
        val calendar = Calendar.getInstance()
        if (item != null && item!!.dueDate != null) calendar.time = item!!.dueDate!!
        dueDateBtn.setOnClickListener {
            val alert = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
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
        if (item != null && item!!.dueDate != null) calendar.time = item!!.dueDate!!
        endTimeBtn.setOnClickListener {
            val alert = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newDate = Date(calendar.timeInMillis)
                endTimeBtn.text = timeFormat.format(newDate)
                endTimeBtn.tag = newDate
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            alert.show()
        }
    }

    private fun setupStartAndEndDateBtns() {
        if (todoType != TodoType.event) return
        val event = item as Event?
        val calendar = Calendar.getInstance()
        if (event != null) calendar.time = event.startDate

        eventStartDate.setOnClickListener {
            val alert = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                eventStartDate.text = dateFormat.format(newDate)
                eventStartDate.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            alert.show()
        }

        eventEndDate.setOnClickListener {
            val alert = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val newDate = Date(calendar.timeInMillis)
                eventEndDate.text = dateFormat.format(newDate)
                eventEndDate.tag = newDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            alert.show()
        }

        eventStartTime.setOnClickListener {
            val alert = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newDate = Date(calendar.timeInMillis)
                eventStartTime.text = timeFormat.format(newDate)
                eventStartTime.tag = newDate
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
            alert.show()
        }

        eventEndTime.setOnClickListener {
            val alert = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                val newDate = Date(calendar.timeInMillis)
                eventEndTime.text = timeFormat.format(newDate)
                eventEndTime.tag = newDate
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

    private fun setupEventAmPmSpinner() {
        var isFromUser = false
        val sessions = mutableListOf<String>("AM", "PM")
        eventStartTimeAmPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        eventStartTimeAmPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    eventStartTimeAmPm.text = sessions[position]
                    eventStartTimeAmPm.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        eventStartTimeAmPm.setOnClickListener {
            isFromUser = true
            eventStartTimeAmPmSpinner.performClick()
        }

        eventEndTimeAmPmSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, sessions)
        eventEndTimeAmPmSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    eventEndTimeAmPm.text = sessions[position]
                    eventEndTimeAmPm.tag = sessions[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        eventEndTimeAmPm.setOnClickListener {
            isFromUser = true
            eventEndTimeAmPmSpinner.performClick()
        }
    }

    private fun setupEventTypeSpinner() {
        var isFromUser = false
        val eventTypes = arrayListOf("Meeting", "Follow up", "New Reachout", "Phone call",
                "Scheduled Meeting", "Personal", "Bag Drop",
                "Social", "One-on-One", "Opportunity Event", "Phone Call",
                "Text",
                "FB Messenger", "Email", "Handwritten Note", "Other")
        eventTypeSpinner.adapter = ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventTypes)
        eventTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFromUser) {
                    eventTypeBtn.text = eventTypes[position]
                    eventTypeBtn.tag = eventTypes[position]
                    isFromUser = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        eventTypeBtn.setOnClickListener {
            isFromUser = true
            eventTypeSpinner.performClick()
        }
    }

    private fun addGoal(goToActions: Boolean = false) {
        val title = todoTitleField.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s_title, todoType.title()),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val request = ActionForGoalRequest()
        request.title = title
        val dueDate = dueDateBtn.text.toString()
        if (dueDate.isNotEmpty())
            request.dueDate = dueDate
        val priority = priorityBtn.tag as String?
        if (!priority.isNullOrEmpty())
            request.priority = priority
        val time = endTimeBtn.text.toString()
        if (time.isNotEmpty())
            request.time = time
        val session = amPmBtn.text.toString()
        if (session.isNotEmpty())
            request.session = session
        request.allDay = "${allDayCheckBox.isChecked}"
        val details = todoDescriptionField.text.toString()
        if (details.isNotEmpty())
            request.details = details
        val contactId = contactField.tag as String?
        if (contactId.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.select_contact),
                    Toast.LENGTH_SHORT).show()
            return
        }
        request.contactId = contactId

        if (todoType == TodoType.goal && actionsBtn.text.isNotEmpty())
            request.goalActions = actionsBtn.tag as String?

        if (todoType != TodoType.event && item != null)
            request.taskId = "${item!!.id}"

        val observable: Observable<HiloResponse<String>>?
        when (todoType) {
            TodoType.action -> observable = HiloApp.api().addAction(request)
            TodoType.goal -> {
                if (item == null)
                    observable = HiloApp.api().addGoal(request)
                else observable = HiloApp.api().updateGoal(request)
            }
            TodoType.need -> observable = HiloApp.api().addTeamNead(request)
            else -> observable = null
        }

        if (observable != null) {
            val loading = showLoading()
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response: HiloResponse<String> ->
                        loading.dismiss()
                        if (response.message.contains("successfully", true)) {
                            if (goToActions) {
                                val assignIntent = Intent(this, AddActionActivity::class.java)
                                assignIntent.putExtra("contactId", contactId)
                                assignIntent.putExtra("goalId", response.data!!)
                                startActivity(assignIntent)
                                finish()
                            } else if (item == null && todoType == TodoType.goal &&
                                    actionsBtn.text.isNotEmpty()) {
                                addActionsToGoal(response.data!!, actionsBtn.tag as String)
                            } else {
                                LocalBroadcastManager.getInstance(this)
                                        .sendBroadcast(Intent(actionUpdateDashboard))
                                finish()
                            }

                            Toast.makeText(this, response.message,
                                    Toast.LENGTH_SHORT).show()
                        } else showExplanation(message = response.message)
                    }, { error: Throwable ->
                        loading.dismiss()
                        error.printStackTrace()
                        showExplanation(message = error.localizedMessage)
                    })
        }
    }

    private fun setupActionsSpinner() {
        val actions = arrayListOf<Tag>()
        val goal = item as Goal?
        if (goal != null) {
            this.actions.forEach { action: ActionItem ->
                action.isSelected = goal.actions.firstOrNull { it.id == action.id } != null
            }
        }
        this.actions.forEach { actions.add(Tag(it.name, it.isSelected)) }
        val adapter = TagsSpinnerAdapter(actions)
        adapter.delegate = object : TagsSpinnerAdapter.TagSpinnerDelegate {
            override fun didClickOnTag(tag: Tag, position: Int) {
                val action = this@CreateTodoActivity.actions[position]
                var currentText = actionsBtn.text.toString()
                var currentTag = actionsBtn.tag as String? ?: ""
                if (currentText.isNotEmpty() && tag.isSelected) {
                    currentText = "$currentText,${action.name}"
                    currentTag = "$currentTag,${action.id}"
                } else if (tag.isSelected) {
                    currentText = action.name
                    currentTag = "${action.id}"
                } else {
                    val regex = "(,)*(${tag.name})(,$)*".toRegex()
                    currentText = regex.replace(currentText, "")
                    currentTag = regex.replace(currentTag, "")
                    if (currentText.startsWith(",")) currentText = "^,".toRegex()
                            .replace(currentText, "")
                    if (currentTag.startsWith(",")) currentTag = "^,".toRegex()
                            .replace(currentText, "")
                }

                actionsBtn.text = currentText
                actionsBtn.tag = currentTag
            }
        }
        actionsSpinner.adapter = adapter
        actionsBtn.setOnClickListener { actionsSpinner.performClick() }
    }

    private fun getContactActions() {
        val loading = showLoading()
        val request = StandardRequest()
        request.contactId = item?.contactId ?: "0"
        HiloApp.api().getContactActions(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<ActionDropDown> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null)
                            this.actions.addAll(data.actionItems)
                        setupActionsSpinner()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })

    }

    private fun saveEvent() {
        val title = todoTitleField.text.toString()
        if (title.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_s_title, todoType.title()),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val eventType = eventTypeBtn.text.toString()
        if (eventType.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_event_type),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val startDate = eventStartDate.text.toString()
        if (startDate.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_start_date),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val startTime = eventStartTime.text.toString()
        if (startDate.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_start_time),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val startAmPm = eventStartTimeAmPm.text.toString()
        if (startDate.isEmpty()) {
            Toast.makeText(this, getString(R.string.select_start_ampm),
                    Toast.LENGTH_SHORT).show()
            return
        }

        val request = SaveEventRequest()
        request.name = title
        request.eventType = eventType
        request.date = startDate
        request.time = startTime
        request.startAmPm = startAmPm
        request.allDay = "${allDayCheckBox.isChecked}"

        if (item != null)
            request.eventId = "${item!!.id}"
        if (eventEndDate.text.isNotEmpty())
            request.endDate = eventEndDate.text.toString()
        if (eventEndTime.text.isNotEmpty())
            request.endTime = eventEndTime.text.toString()
        if (eventEndTimeAmPm.text.isNotEmpty())
            request.endAmPm = eventEndTimeAmPm.text.toString()
        if (locationLabel.text.isNotEmpty())
            request.location = locationLabel.text.toString()
        if (contactField.text.isNotEmpty())
            request.contactId = contactField.tag as String?
        if (todoDescriptionField.text.isNotEmpty())
            request.details = todoDescriptionField.text.toString()

        val loading = showLoading()
        HiloApp.api().saveEvent(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.message.contains("successfully", true)) {
                        LocalBroadcastManager.getInstance(this)
                                .sendBroadcast(Intent(actionUpdateDashboard))
                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    private fun addActionsToGoal(goalId: String, actions: String) {
        val request = ActionToGoalRequest()
        request.goalId = goalId
        request.actions = actions

        val loading = showLoading()
        HiloApp.api().addActionToGoal(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<String> ->
                    loading.dismiss()
                    if (response.status.isSuccess()) {
                        LocalBroadcastManager.getInstance(this)
                                .sendBroadcast(Intent(actionUpdateDashboard))
                        finish()
                    } else showExplanation(message = response.message)
                }, { error: Throwable ->
                    loading.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ActionDropDown(@JsonProperty("ActionDropdown") val actionItems: ArrayList<ActionItem>)

@JsonIgnoreProperties(ignoreUnknown = true)
class ActionItem(@JsonProperty("id") val id: Int,
                 @JsonProperty("name") val name: String) {
    var isSelected = false
}
