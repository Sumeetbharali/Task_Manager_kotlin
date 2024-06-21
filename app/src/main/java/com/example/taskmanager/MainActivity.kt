package com.example.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDatabase
import com.example.taskmanager.repository.TaskRepository
import com.example.taskmanager.ui.TaskAdapter
import com.example.taskmanager.ui.TaskViewModel
import com.example.taskmanager.ui.TaskViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        val repository = TaskRepository(taskDao)
        val viewModelFactory = TaskViewModelFactory(repository)
        val taskViewModel: TaskViewModel by viewModels { viewModelFactory }
        this.taskViewModel = taskViewModel

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        adapter = TaskAdapter { task -> showEditDialog(task) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        taskViewModel.allTasks.observe(this, { tasks ->
            tasks?.let { adapter.updateTasks(it) }
        })

        val buttonAdd = findViewById<Button>(R.id.button_add)
        val editTextTask = findViewById<EditText>(R.id.edit_text_task)
        buttonAdd.setOnClickListener {
            val taskName = editTextTask.text.toString()
            if (taskName.isNotEmpty()) {
                val newTask = Task(taskName = taskName)
                taskViewModel.insert(newTask)
                editTextTask.text.clear()
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = adapter.tasksList[viewHolder.adapterPosition]
                taskViewModel.delete(task)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showEditDialog(task: Task) {
        val editText = EditText(this).apply { setText(task.taskName) }
        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Task")
            .setView(editText)
            .setPositiveButton("Save") { dialog, which ->
                val updatedTask = task.copy(taskName = editText.text.toString())
                taskViewModel.update(updatedTask)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
