package com.example.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.data.models.Converter
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.example.todoapp.databinding.FragmentAddBinding
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.SharedViewModelFactory
import com.example.todoapp.utils.hideKeyBoard


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }

    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            titleEt.setOnKeyListener{ _, keyCode, _ ->
                handelKeyEvent(keyCode)
            }
            descriptionEt.setOnKeyListener { _, keyCode, _->
                handelKeyEvent(keyCode)
            }
            prioritiesSpinner.onItemSelectedListener = sharedViewModel.spinnerListener
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = binding.titleEt.text.toString()
        val description = binding.descriptionEt.text.toString()
        val validate = sharedViewModel.verifyDataFromUser(title, description)
        if (validate) {
            val toDoModel = ToDoModel(
                title,
                Converter().toPriority(binding.prioritiesSpinner.selectedItem.toString().substringBefore(" ")),
                description
            )
            toDoViewModel.insertData(toDoModel)
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
            Toast.makeText(
                requireContext(),
                getString(R.string.toast_successfully_add),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handelKeyEvent(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            hideKeyBoard(requireActivity())
            return true
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}