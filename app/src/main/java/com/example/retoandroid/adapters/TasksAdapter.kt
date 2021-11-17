package com.example.retoandroid.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retoandroid.interfaces.IButtonTask
import com.example.retoandroid.models.Task
import androidx.annotation.RequiresApi
import com.example.retoandroid.R


class TasksAdapter(
    private val data: ArrayList<Task>
) : RecyclerView.Adapter<TasksAdapter.TaskAdapterViewHolder>() {
    lateinit var iButtonTask: IButtonTask
    private lateinit var task: Task

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskAdapterViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskAdapterViewHolder(v)
    }

    @SuppressLint("NewApi", "SetTextI18n")
    override fun onBindViewHolder(holder: TaskAdapterViewHolder, position: Int) {
        task = data[position]
        holder.textViewTaskName.text = task.taskName
        holder.imageViewMoreAction.setOnClickListener {
            openOptionMenu(it, position)
        }
        holder.bindTask(task)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun openOptionMenu(it: View?, position: Int) {
        val popup = PopupMenu(it!!.context, it)
        popup.menuInflater.inflate(R.menu.menu_task, popup.menu)
        popup.setForceShowIcon(true)
        popup.setOnMenuItemClickListener { item ->

            when (item.itemId) {
                R.id.action_edit -> iButtonTask.editTask(data[position])
                R.id.action_delete -> iButtonTask.deleteTask(data[position])
            }

            true
        }
        popup.show()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class TaskAdapterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var textViewTaskName: TextView =
            itemView.findViewById(R.id.txt_task_name_adapter)

        var imageViewMoreAction: ImageButton =
            itemView.findViewById(R.id.button_more)


        private var task: Task? = null

        fun bindTask(task: Task) {
            this.task =
                task
        }

        override fun onClick(v: View) {

        }

    }
}


