package com.hiloipa.app.hilo.ui.tracker

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Spinner
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.adapter.FutureContactsAdapter
import com.hiloipa.app.hilo.adapter.GoalTrackerAdapter
import com.hiloipa.app.hilo.models.GoalType
import com.hiloipa.app.hilo.models.requests.StandardRequest
import com.hiloipa.app.hilo.models.responses.*
import com.hiloipa.app.hilo.ui.contacts.ContactDetailsActivity
import com.hiloipa.app.hilo.ui.widget.RalewayButton
import com.hiloipa.app.hilo.ui.widget.RalewayEditText
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import com.hiloipa.app.hilo.utils.HiloApp
import com.hiloipa.app.hilo.utils.isSuccess
import com.hiloipa.app.hilo.utils.showExplanation
import com.hiloipa.app.hilo.utils.showLoading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_future_contacts.*

class FutureContactsActivity : AppCompatActivity(), GoalTrackerAdapter.ContactClickListener {

    lateinit var adapter: FutureContactsAdapter
    lateinit var goalType: GoalType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_future_contacts)
        toolbar.setNavigationOnClickListener { finish() }

        // get goal type from intent
        val intGoalType = intent.extras.getInt("goalType")
        goalType = GoalType.fromInt(intGoalType)
        // set toolbar title according to goal type
        toolbarTitle.text = getString(goalType.title())
        // set list header text according to goal type
        if (goalType != GoalType.reach_outs)
            futureDescriptionLabel.text = getString(goalType.description())
        else futureDescriptionLabel.visibility = View.GONE
        // create adapter and setup recycler view
        adapter = FutureContactsAdapter(this, goalType)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        // get contacts from server
        when (goalType) {
            GoalType.reach_outs -> this.getAllTrackerData<ReachOutContact>(goalType)
            GoalType.follow_ups -> this.getAllTrackerData<FollowUpContact>(goalType)
            GoalType.team_reach_outs -> this.getAllTrackerData<TeamReachOutContact>(goalType)
        }
    }


    private fun <T: Contact> getAllTrackerData(goalType: GoalType) {
        val dialog = showLoading()
        val request = StandardRequest()
        request.type = goalType.apiValue()

        HiloApp.api().getFutureFollowUps(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: HiloResponse<FutureFollowUps> ->
                    dialog.dismiss()
                    if (response.status.isSuccess()) {
                        val data = response.data
                        if (data != null) {
                            when (goalType) {
                                GoalType.follow_ups ->
                                    adapter.updateAdapterData<FollowUpContact>(data.backlogs)
                            }
                        }
                    } else
                        showExplanation(message = response.message)
                }, { error: Throwable ->
                    dialog.dismiss()
                    error.printStackTrace()
                    showExplanation(message = error.localizedMessage)
                })
    }

    override fun onCompleteClicked() {
        when(goalType) {
            GoalType.follow_ups, GoalType.reach_outs -> this.showCompleteReachOutDialog()
            GoalType.team_reach_outs -> this.showCompleteTeamReachOutDialog()
        }
    }

    override fun onDeleteClicked() {

    }

    override fun onContactClicked() {
        val intent = Intent(this, ContactDetailsActivity::class.java)
        this.startActivity(intent)
    }

    private fun showCompleteReachOutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_complete_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val title: RalewayTextView = dialogView.findViewById(R.id.completeReachOutTile)
        val leadTempBtn: RalewayButton = dialogView.findViewById(R.id.leadTempBtn)
        val leadTempSpinner: Spinner = dialogView.findViewById(R.id.leadTempSpinner)
        val pipelinePos: RalewayButton = dialogView.findViewById(R.id.updatePipelinePositionBtn)
        val pipelineSpinner: Spinner = dialogView.findViewById(R.id.updatePipelinePositionSpinner)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val scheduleNextFollowUp: RalewayButton = dialogView.findViewById(R.id.scheduleNextFollowUpBtn)
        val scheduleNextFollowUpSpinner: Spinner = dialogView.findViewById(R.id.scheduleNextFollowUpSpinner)
        val contactType: RalewayButton = dialogView.findViewById(R.id.contactTypeBtn)
        val contactTypeSpinner: Spinner = dialogView.findViewById(R.id.contactTypeSpinner)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showCompleteTeamReachOutDialog() {
        val dialogView = layoutInflater.inflate(R.layout.alert_complete_team_reach_out, null)
        val backBtn: RalewayButton = dialogView.findViewById(R.id.completeReachOutBackBtn)
        val logReachOutType: RalewayButton = dialogView.findViewById(R.id.logReachOutTypeBtn)
        val logReachOutTypeSpinner: Spinner = dialogView.findViewById(R.id.logReachOutTypeSpinner)
        val logReachOutComment: RalewayEditText = dialogView.findViewById(R.id.logReachOutCommentsField)
        val completeBtn: RalewayButton = dialogView.findViewById(R.id.completeBtn)

        val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()
        completeBtn.setOnClickListener { dialog.dismiss() }
        backBtn.setOnClickListener { dialog.dismiss() }
        dialog.setCanceledOnTouchOutside(false)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}
