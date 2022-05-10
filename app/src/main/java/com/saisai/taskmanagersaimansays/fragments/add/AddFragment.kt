package com.saisai.taskmanagersaimansays.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.saisai.taskmanagersaimansays.R
import com.saisai.taskmanagersaimansays.databinding.FragmentAddBinding
import com.saisai.taskmanagersaimansays.task.Task
import com.saisai.taskmanagersaimansays.view_model.TaskViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)


        binding.apply {
            taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

            btnAdd.setOnClickListener {
                insertToDB()
            }

            btnBackAdd.setOnClickListener {
                findNavController().navigate(R.id.action_addFragment_to_listFragment)
            }

        }

        return binding.root
    }

    // insert task to database
    private fun insertToDB() {
        val title = binding.editTextAddTitle.text.toString()
        val task = binding.editTextAddTask.text.toString()

        if(inputCheck(title, task)) {
            val todo = Task(0, title, task, false)
            taskViewModel.addTask(todo)

            Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "The input fields can't be empty!", Toast.LENGTH_SHORT).show()
        }

    }

    // if text input fields aren't empty
    private fun inputCheck(title: String, task: String) : Boolean {
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(task))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}