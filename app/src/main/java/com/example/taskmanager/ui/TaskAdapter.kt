package com.example.taskmanager.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.Task

class TaskAdapter(private val onEditClick: (Task) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var tasksList = mutableListOf<Task>()

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.text_view_task)
        val taskDateView: TextView = itemView.findViewById(R.id.text_view_date)
        val moreOptions: ImageView = itemView.findViewById(R.id.image_view_more)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasksList[position]
        holder.taskTextView.text = currentTask.taskName
        holder.taskDateView.text = "2 Days ago" // Update this accordingly
        holder.moreOptions.setOnClickListener { onEditClick(currentTask) }
    }

    override fun getItemCount() = tasksList.size

    fun updateTasks(tasks: List<Task>) {
        this.tasksList = tasks.toMutableList()
        notifyDataSetChanged()
    }

    fun addTask(task: Task) {
        tasksList.add(0, task)
        notifyItemInserted(0)
    }

    fun removeTask(task: Task) {
        val position = tasksList.indexOf(task)
        if (position >= 0) {
            tasksList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
