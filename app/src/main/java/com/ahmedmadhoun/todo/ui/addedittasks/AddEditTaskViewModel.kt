package com.ahmedmadhoun.todo.ui.addedittasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmedmadhoun.todo.data.Task
import com.ahmedmadhoun.todo.data.TaskDao
import com.ahmedmadhoun.todo.ui.ADD_TASK_RESULT_OK
import com.ahmedmadhoun.todo.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

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

    fun onSaveClick() {
        if (taskTitle.isBlank()) {
            // Send invalid input message event
            showInvalidInputMessage("Title cannot be empty")
            return
        }

        if (task != null) {
            // Update Task and Send navigate back with result event
            val task = task.copy(title = taskTitle, important = taskImportance)
            updateTask(task)
        } else {
            // Add Task and Send navigate back with result event
            val newTask = Task(title = taskTitle, important = taskImportance)
            addNewTask(newTask)
        }
    }

    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(message))
    }


    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.updateTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    private fun addNewTask(newTask: Task) = viewModelScope.launch {
        taskDao.insertTask(newTask)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

}