package com.saisai.taskmanagersaimansays.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saisai.taskmanagersaimansays.R
import com.saisai.taskmanagersaimansays.databinding.FragmentListBinding
import com.saisai.taskmanagersaimansays.task.Task
import com.saisai.taskmanagersaimansays.view_model.TaskViewModel


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel

    // for the searchView
    private val myAdapter: ListAdapter by lazy { ListAdapter() }

    // Animation
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.btn_rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.btn_rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.btn_from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.btn_to_bottom_anim)
    }
    private val alpha0To1: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_0_to_1)
    }
    private val alpha1To0: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.alpha_1_to_0)
    }
    private var clickedAddButton = false
    private var clickedInfoButton = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.apply {

            // recyclerView
            //this adapter is for retrieving the recyclerView
            val adapter = ListAdapter()
            val recyclerView = recyclerView
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            // taskViewModel to read data from database
            taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
            taskViewModel.readAllData.observe(viewLifecycleOwner) { task ->
                adapter.setData(task)
            }

            // add buttons
            fabAdd.setOnClickListener {
                onAddButtonClicked()
            }
            btnTask.setOnClickListener {
                onAddButtonClicked()
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
            btnSaimanSays.setOnClickListener {
                saimanSaysQuotes()
            }

            // delete all button
            btnDeleteAll.setOnClickListener {
                deleteAllTasks()
            }

            // info button
            btnInfo.setOnClickListener {
                onInfoButtonClicked()
            }

            // swipe left to delete - swipeDelete & itemTouchHelper
            val swipeDelete = object : SwipeDelete(requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    taskViewModel.deleteTask(adapter.getNoteAt(viewHolder.bindingAdapterPosition))
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeDelete)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            // searchView
            searchView.isSubmitButtonEnabled = true
            searchView.setOnQueryTextListener(this@ListFragment)

        }
        return binding.root
    }

    // SearchView
    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }
    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }
    // %$query% - % around query is needed for searching SQL room
    private fun searchDatabase(query: String) {

        val searchQuery = "%$query%"
        binding.recyclerView.adapter = myAdapter

        taskViewModel.searchDatabase(searchQuery).observe(this) { list ->
            list.let {
                myAdapter.setData(it)
            }
        }
    }

    //Add button animation
    private fun onAddButtonClicked() {
        setAddVisibility()
        setAddAnimation()
        setAddClickable()
        clickedAddButton = !clickedAddButton
    }
    private fun setAddVisibility() {
        if(!clickedAddButton) {
            binding.btnTask.visibility = View.VISIBLE
            binding.btnSaimanSays.visibility = View.VISIBLE
        } else {
            binding.btnTask.visibility = View.GONE
            binding.btnSaimanSays.visibility = View.GONE
        }
    }
    private fun setAddAnimation() {
        if(!clickedAddButton) {
            binding.btnTask.startAnimation(fromBottom)
            binding.btnSaimanSays.startAnimation(fromBottom)
            binding.fabAdd.startAnimation(rotateOpen)
        } else {
            binding.btnTask.startAnimation(toBottom)
            binding.btnSaimanSays.startAnimation(toBottom)
            binding.fabAdd.startAnimation(rotateClose)
        }
    }
    private fun setAddClickable() {
        if(!clickedAddButton) {
            binding.btnTask.isClickable = true
            binding.btnSaimanSays.isClickable = true
        } else {
            binding.btnTask.isClickable = false
            binding.btnSaimanSays.isClickable = false
        }
    }

    // info button animation
    private fun onInfoButtonClicked() {
        setInfoVisibility()
        setInfoAnimation()
        clickedInfoButton = !clickedInfoButton
    }
    private fun setInfoVisibility() {
        if(!clickedInfoButton) {
            binding.cardViewInfo.visibility = View.VISIBLE
        } else {
            binding.cardViewInfo.visibility = View.GONE
        }
    }
    private fun setInfoAnimation() {
        if(!clickedInfoButton) {
            binding.cardViewInfo.startAnimation(alpha0To1)
        } else {
            binding.cardViewInfo.startAnimation(alpha1To0)
        }
    }

    // delete all tasks
    private fun deleteAllTasks() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Delete") { _, _ ->
            taskViewModel.deleteAllTasks()
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setTitle( "Delete All Tasks?")
        builder.create().show()
    }

    // saiman says generator
    private fun saimanSaysQuotes() {
        val quotesList = arrayListOf(
            Task(0, "Saiman Says", "Take a deep breath and say I am who I am, and that’s great",false),
            Task(0, "Saiman Says", "Think of a person you care about and text that person a list of whys", false),
            Task(0, "Saiman Says", "Call a friend.", false),
            Task(0, "Saiman Says", "Time to take a break!", false),
            Task(0, "Saiman Says", "There’s always room for some deep breathing, so take a moment and inhale through your nose and exhale through your mouth/nose.", false),
            Task(0, "Saiman Says", "Food is life. Cook or buy your favourite meal.", false),
            Task(0, "Saiman Says", "Now is the time to learn something new. Learn something new.", false),
            Task(0, "Saiman Says", "Everything becomes easier with a smoothie in your hand - have a smoothie.", false),
            Task(0, "Saiman Says", "Are your surroundings messy, tidy up!", false),
            Task(0, "Saiman Says", "Put on your fave song and dance, dance, dance!", false),
            Task(0, "Saiman Says", "Compliment someone you care about.", false),
            Task(0, "Saiman Says", "Eat a fruit!", false)
        )
        val randomElement = quotesList.random()
        taskViewModel.addTask(randomElement)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}