package com.buenobueno.todoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.buenobueno.todoapp.R
import com.buenobueno.todoapp.database.TodoTask

class RecyclerAdapter(private var todoTasks: MutableList<TodoTask>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var listener: OnTodoTaskClickListener? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoTasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = todoTasks[position]
        holder.textView.text = "${task.title} : ${task.id}"
        holder.itemView.setOnClickListener { v: View? ->
            if (listener != null) {
                listener!!.onTaskClick(task)
            }
        }
    }

    interface OnTodoTaskClickListener {
        fun onTaskClick(todoTask: TodoTask?)
    }

    fun setOnTodoTaskClickListener(listener: OnTodoTaskClickListener?) {
        this.listener = listener
    }

    fun setData(tasks: MutableList<TodoTask>) {
        this.todoTasks = tasks
        notifyDataSetChanged()
    }

    fun getTaskAtPosition(position: Int): TodoTask {
        return todoTasks[position]
    }

    fun removeTaskAtPosition(position: Int) {
        todoTasks.removeAt(position)
        notifyItemRemoved(position)
    }


}