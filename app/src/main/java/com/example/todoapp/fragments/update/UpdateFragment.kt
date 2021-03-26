package com.example.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import android.view.*
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.Utils
import com.example.todoapp.data.models.Converter
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.SharedViewModelFactory

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(requireActivity().application)
    }
    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }
    private lateinit var currentTitle: TextView
    private lateinit var currentDescription: TextView
    private lateinit var currentPriority: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        view.apply {
            currentTitle = findViewById(R.id.current_title_et)
            currentDescription = findViewById(R.id.current_description_et)
            currentPriority = findViewById(R.id.current_priorities_spinner)

            currentTitle.text = args.currentItem.title
            currentDescription.text = args.currentItem.description
            currentPriority.setSelection(args.currentItem.priority.ordinal)
            currentPriority.onItemSelectedListener = sharedViewModel.spinnerListener
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                updateModel()
                Utils().hideKeyBoard(requireActivity())
            }
            R.id.menu_delete -> {
                confirmDeleteModel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteModel() {
        val currentTitle = args.currentItem.title
        AlertDialog.Builder(requireContext()).apply {
            setPositiveButton("Yes") { _, _ ->
                toDoViewModel.deleteModel(args.currentItem)
                goToBackScreen()
                showToastMessage(getString(R.string.toast_successfully_delete_model, currentTitle))
            }
            setNegativeButton("No") {_,_ ->}
            setTitle(getString(R.string.dialog_delete_title, currentTitle))
            setMessage(getString(R.string.dialog_delete_message, currentTitle))
        }.create().show()

    }

    private fun updateModel() {
        val title = currentTitle.text.toString()
        val description = currentDescription.text.toString()
        val validate = sharedViewModel.verifyDataFromUser(title, description)
        if (validate) {
            val toDoModel = ToDoModel(
                title,
                Converter().toPriority(
                    currentPriority.selectedItem.toString().substringBefore(" ")
                ),
                description,
                args.currentItem.id
            )
            toDoViewModel.updateModel(toDoModel)
            goToBackScreen()
            showToastMessage(getString(R.string.toast_successfully_update))
        }
    }

    private fun goToBackScreen() {
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }
    private fun showToastMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}