package com.example.retoandroid.interfaces

import com.example.retoandroid.models.Task

interface IButtonTask {

    fun editTask(task: Task)
    fun deleteTask(task: Task)

}