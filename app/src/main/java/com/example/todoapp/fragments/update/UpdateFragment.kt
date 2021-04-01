package com.example.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.data.models.Converter
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.example.todoapp.databinding.FragmentUpdateBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.SharedViewModelFactory

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(requireActivity().application)
    }
    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            currentTitleEt.setText(args.currentItem.title)
            currentDescriptionEt.setText(args.currentItem.description)
            currentPrioritiesSpinner.setSelection(args.currentItem.priority.ordinal)
            currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.spinnerListener
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                updateModel()
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
            setNegativeButton("No") { _, _ -> }
            setTitle(getString(R.string.dialog_delete_title, currentTitle))
            setMessage(getString(R.string.dialog_delete_message, currentTitle))
        }.create().show()
    }

    private fun updateModel() {
        val title = binding.currentTitleEt.text.toString()
        val description = binding.currentDescriptionEt.text.toString()
        val validate = sharedViewModel.verifyDataFromUser(title, description)
        if (validate) {
            val toDoModel = ToDoModel(
                title,
                Converter().toPriority(
                    binding.currentPrioritiesSpinner.selectedItem.toString().substringBefore(" ")
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}