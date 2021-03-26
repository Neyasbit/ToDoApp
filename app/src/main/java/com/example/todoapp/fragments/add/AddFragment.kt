package com.example.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.ToDoApplication
import com.example.todoapp.Utils
import com.example.todoapp.data.models.Converter
import com.example.todoapp.data.models.ToDoModel
import com.example.todoapp.data.viewmodels.ToDoViewModel
import com.example.todoapp.data.viewmodels.ToDoViewModelFactory
import com.example.todoapp.fragments.SharedViewModel
import com.example.todoapp.fragments.SharedViewModelFactory


class AddFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels {
        ToDoViewModelFactory((requireActivity().application as ToDoApplication).repository)
    }

    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(requireActivity().application)
    }
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var priority: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)


        view.apply {
            title = findViewById(R.id.title_et)
            description = findViewById(R.id.description_et)
            priority = findViewById(R.id.priorities_spinner)
        }

        title.setOnKeyListener{_, ketCode, _ ->
            handelKeyEvent(ketCode)
        }
        description.setOnKeyListener{ _, ketCode, _ ->
            handelKeyEvent(ketCode)
        }
        priority.onItemSelectedListener = sharedViewModel.spinnerListener
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
            Utils().hideKeyBoard(requireActivity())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = title.text.toString()
        val description = description.text.toString()
        val validate = sharedViewModel.verifyDataFromUser(title, description)
        if (validate) {
            val toDoModel = ToDoModel(
                title,
                Converter().toPriority(priority.selectedItem.toString().substringBefore(" ")),
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
            Utils().hideKeyBoard(requireActivity())
            return true
        }
        return false
    }

}