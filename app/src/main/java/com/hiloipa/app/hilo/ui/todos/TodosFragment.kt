package com.hiloipa.app.hilo.ui.todos


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hiloipa.app.hilo.R
import kotlinx.android.synthetic.main.fragment_todos.*


/**
 * A simple [Fragment] subclass.
 */
class TodosFragment : Fragment() {

    companion object {
        fun newInstance(): TodosFragment {
            val args = Bundle()
            val fragment = TodosFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater!!.inflate(R.layout.fragment_todos, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalsButton.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.goals)).
                show(childFragmentManager, "GoalsFragment") }

        actionsButton.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.actions))
                .show(childFragmentManager, "ActionsFragment") }

        teamNeedsBtn.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.team_needs))
                .show(childFragmentManager, "TeamNeedsFragment") }

        calendarEvetsBtn.setOnClickListener { TodoDetailsFragment.newInstance(getString(R.string.calendar_events), showCheckBox = false)
                .show(childFragmentManager, "CalendarEventsFragment") }
    }
}
