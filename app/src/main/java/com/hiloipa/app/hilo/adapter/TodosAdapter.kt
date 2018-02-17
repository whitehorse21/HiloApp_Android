package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.ui.widget.RalewayTextView

/**
 * Created by eduardalbu on 17.02.2018.
 */
class TodosAdapter(val context: Context, val showCheckBox: Boolean = true): RecyclerView.Adapter<TodosAdapter.ViewHolder>() {

    var delegate: TodoDelegate? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        holder.todoCheckBox.visibility = if (showCheckBox) View.VISIBLE else View.GONE
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val todoLabel: RalewayTextView = itemView.findViewById(R.id.todoLabel)
        val todoCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.todoCheckBox)
        val editBtn: ImageButton = itemView.findViewById(R.id.todoEditBtn)

        init {
            itemView.setOnClickListener { delegate?.onItemClicked() }

            editBtn.setOnClickListener { delegate?.onTodoEditClicked() }

            todoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                delegate?.onTodoCheckedChanged(isChecked)
            }
        }
    }

    interface TodoDelegate {
        fun onItemClicked()
        fun onTodoCheckedChanged(isChecked: Boolean)
        fun onTodoEditClicked()
    }
}