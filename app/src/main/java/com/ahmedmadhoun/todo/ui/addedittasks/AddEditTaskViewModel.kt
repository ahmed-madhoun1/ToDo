package com.ahmedmadhoun.todo.ui.addedittasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ahmedmadhoun.todo.data.Task
import com.ahmedmadhoun.todo.data.TaskDao

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val task = state.get<Task>("task")

    var taskTitle = state.get<String>("taskTitle") ?: task?.title ?: ""
    set(value) {
        field = value
        state.set("taskTitle", value)
    }
    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
    set(value) {
        field = value
        state.set("taskImportance", value)
    }

}