package com.example.retoandroid.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retoandroid.models.Task
import com.example.retoandroid.R
import com.example.retoandroid.TASKS
import com.example.retoandroid.USERNAME
import com.example.retoandroid.adapters.TasksAdapter
import com.example.retoandroid.databinding.DialogNewTaskBinding
import com.example.retoandroid.databinding.FragmentTasksBinding
import com.example.retoandroid.interfaces.IButtonTask
import com.example.retoandroid.viewModels.LoginViewModel
import com.example.retoandroid.viewModels.TasksViewModel
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class TasksFragment : Fragment(), IButtonTask {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var bindingDialog: DialogNewTaskBinding
    private lateinit var bindingDialogEditTask: DialogNewTaskBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var taskViewModel: TasksViewModel
    private lateinit var username: String
    private lateinit var urlImage: String
    private val args: TasksFragmentArgs by navArgs()
    private val File = 1
    private lateinit var taskAdapter: TasksAdapter
    private var arrayTaskList: ArrayList<Task> = ArrayList()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loginViewModel = LoginViewModel()
        taskViewModel = TasksViewModel()
        username = args.username


        binding.buttonNewTask.setOnClickListener {
            showDialogNewTask()
        }
        getTasks(username)

    }

    private fun setAdapterTask(taskList: ArrayList<Task>) {

        taskAdapter = TasksAdapter(taskList)
        taskAdapter.iButtonTask = this
        binding.recyclerViewTask.adapter = taskAdapter
        binding.recyclerViewTask.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    fun getTasks(username: String) {

        firestore.collection(TASKS)
            .whereEqualTo(USERNAME, username)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Log.e("firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            arrayTaskList.add(dc.document.toObject(Task::class.java))
                        }
                        if (dc.type == DocumentChange.Type.REMOVED) {
                            arrayTaskList.remove(dc.document.toObject(Task::class.java))
                        }
                        if (dc.type == DocumentChange.Type.MODIFIED) {
                            arrayTaskList[dc.newIndex] = dc.document.toObject(Task::class.java)
                        }
                    }
                    setAdapterTask(arrayTaskList)
                    taskAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun showDialogNewTask() {

        bindingDialog =
            DialogNewTaskBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = AlertDialog.Builder(context)
        dialog.setView(bindingDialog.root)
        dialog.setCancelable(false)

        val dialogShow = dialog.create()

        bindingDialog.imageViewInsert.setOnClickListener {
            fileUpload()
        }
        bindingDialog.buttonCreateTask.setOnClickListener {
            val task = Task(
                username = username,
                taskName = bindingDialog.txtTaskName.text.toString(),
                taskDescription = bindingDialog.txtDescription.text.toString(),
                url = urlImage
            )
            taskViewModel.postTask(task)
            dialogShow.dismiss()
        }

        bindingDialog.buttonClose.setOnClickListener {
            dialogShow.cancel()
        }

        dialogShow.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogShow.show()
    }

    private fun fileUpload() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, File)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == File) {
            if (resultCode == RESULT_OK) {
                val FileUri = data!!.data
                val Folder: StorageReference =
                    FirebaseStorage.getInstance().getReference().child("Photo")
                val file_name: StorageReference = Folder.child("file" + FileUri!!.lastPathSegment)
                file_name.putFile(FileUri).addOnSuccessListener { taskSnapshot ->
                    file_name.getDownloadUrl().addOnSuccessListener { uri ->
                        urlImage = java.lang.String.valueOf(uri)
                        Picasso.get()
                            .load(urlImage)
                            .placeholder(R.drawable.ic_baseline_image_24)
                            .error(R.drawable.ic_baseline_error_24)
                            .fit()
                            .into(bindingDialog.imageViewInsert)
                    }
                }
            }
        }
    }

    override fun editTask(task: Task) {
        bindingDialogEditTask =
            DialogNewTaskBinding.inflate(LayoutInflater.from(requireContext()))

        val dialog = AlertDialog.Builder(context)
        dialog.setView(bindingDialogEditTask.root)
        dialog.setCancelable(false)

        val dialogShow = dialog.create()

        bindingDialogEditTask.txtTaskName.setText(task.taskName)
        bindingDialogEditTask.txtDescription.setText(task.taskDescription)

        Picasso.get()
            .load(task.url)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_error_24)
            .fit()
            .into(bindingDialogEditTask.imageViewInsert)

        bindingDialogEditTask.imageViewInsert.setOnClickListener {
            fileUpload()
        }
        bindingDialogEditTask.buttonCreateTask.setOnClickListener {

            val taskNew = Task(
                username = username,
                taskName = bindingDialogEditTask.txtTaskName.text.toString(),
                taskDescription = bindingDialogEditTask.txtDescription.text.toString(),
                url = task.url
            )
            taskViewModel.editTask(task.taskName.toString(), taskNew)
            dialogShow.dismiss()
        }


        bindingDialogEditTask.buttonClose.setOnClickListener {
            dialogShow.cancel()
        }

        dialogShow.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogShow.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteTask(task: Task) {
        taskViewModel.deleteTask(task)
        taskAdapter.notifyDataSetChanged()
    }


}