package com.example.taskmanager.repository

import androidx.lifecycle.LiveData
import com.example.taskmanager.data.Task
import com.example.taskmanager.data.TaskDao

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insert(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    suspend fun deleteAll() {
        taskDao.deleteAll()
    }

    suspend fun update(task: Task) {
        taskDao.update(task)
    }
}
