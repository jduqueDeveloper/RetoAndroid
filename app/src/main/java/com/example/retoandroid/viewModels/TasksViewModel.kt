package com.example.retoandroid.viewModels

import androidx.lifecycle.ViewModel
import com.example.retoandroid.models.Task
import com.example.retoandroid.TASKS
import com.example.retoandroid.TASK_NAME
import com.google.firebase.firestore.*

class TasksViewModel : ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun postTask(task: Task) {
        firestore.collection(TASKS).add(task)
    }

    fun editTask(taskName: String, taskNew: Task) {
        firestore.collection(TASKS)
            .get().addOnSuccessListener {
                it.forEach {
                    if (taskName == it.get(TASK_NAME)) {
                        it.reference.set(taskNew)
                    }
                }
            }
    }

    fun deleteTask(task: Task) {
        firestore.collection(TASKS)
            .get().addOnSuccessListener {
                it.forEach {
                    if (task.taskName == it.get(TASK_NAME)) {
                        it.reference.delete()
                    }
                }
            }
    }
}