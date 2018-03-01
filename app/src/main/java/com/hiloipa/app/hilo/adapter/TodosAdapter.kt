package com.hiloipa.app.hilo.adapter

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.hiloipa.app.hilo.R
import com.hiloipa.app.hilo.models.responses.Event
import com.hiloipa.app.hilo.models.responses.ToDo
import com.hiloipa.app.hilo.models.responses.TodoType
import com.hiloipa.app.hilo.ui.widget.RalewayTextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by eduardalbu on 17.02.2018.
 */
class TodosAdapter<T: ToDo>(val context: Context, val type: TodoType, val data: ArrayList<T>):
        RecyclerView.Adapter<TodosAdapter<T>.ViewHolder>() {

    var delegate: TodoDelegate? = null
    val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm aaa", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        if (holder == null) return
        val item: T = data[position]
        holder.toDo = item
        holder.todoCheckBox.visibility = if (type.needCheckBox()) View.VISIBLE else View.GONE
        holder.dateLabel.visibility = if (type.needDate()) View.VISIBLE else View.GONE
        holder.todoLabel.text = item.name
        if (type == TodoType.event) {
            val event = item as Event
            holder.dateLabel.text = dateFormat.format(event.startDate)
        }
        holder.todoCheckBox.isChecked = item.isSelected
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val todoLabel: RalewayTextView = itemView.findViewById(R.id.todoLabel)
        val todoCheckBox: AppCompatCheckBox = itemView.findViewById(R.id.todoCheckBox)
        val editBtn: ImageButton = itemView.findViewById(R.id.todoEditBtn)
        val dateLabel: RalewayTextView = itemView.findViewById(R.id.todoDateLabel)
        lateinit var toDo: T

        init {
            itemView.setOnClickListener { delegate?.onItemClicked(toDo, adapterPosition) }

            editBtn.setOnClickListener { delegate?.onTodoEditClicked(toDo, adapterPosition) }

            todoCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                toDo.isSelected = isChecked
                delegate?.onTodoCheckedChanged(toDo, adapterPosition)
            }
        }
    }

    interface TodoDelegate {
        fun onItemClicked(toDo: ToDo, position: Int)
        fun onTodoCheckedChanged(toDo: ToDo, position: Int)
        fun onTodoEditClicked(toDo: ToDo, position: Int)
    }
}