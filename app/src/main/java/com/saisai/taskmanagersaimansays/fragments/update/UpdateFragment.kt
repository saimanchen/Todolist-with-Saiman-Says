package com.saisai.taskmanagersaimansays.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.saisai.taskmanagersaimansays.R
import com.saisai.taskmanagersaimansays.databinding.FragmentUpdateBinding

import com.saisai.taskmanagersaimansays.task.Task
import com.saisai.taskmanagersaimansays.view_model.TaskViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        binding.apply {
            editTextUpdateTitle.setText(args.currentTask.title)
            editTextUpdateTask.setText(args.currentTask.task)

            btnUpdate.setOnClickListener {
                updateItem()
            }

            btnBackUpdate.setOnClickListener {
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
        }
        return binding.root
    }

    private fun updateItem() {
        val title = binding.editTextUpdateTitle.text.toString()
        val task = binding.editTextUpdateTask.text.toString()

        if(inputCheck(title, task)) {
            val updatedTask = Task(args.currentTask.id, title, task, false)
            taskViewModel.updateTask(updatedTask)
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "The input fields can't be empty!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck(title: String, task: String) : Boolean {

        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(task))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}