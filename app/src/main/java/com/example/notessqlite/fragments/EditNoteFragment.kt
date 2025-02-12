package com.example.notessqlite.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notessqlite.MainActivity
import com.example.notessqlite.R
import com.example.notessqlite.databinding.FragmentAddNoteBinding
import com.example.notessqlite.databinding.FragmentEditNoteBinding
import com.example.notessqlite.model.Note
import com.example.notessqlite.viewmodel.NoteViewModel



// Fragment for editing a note
class EditNoteFragment : Fragment(R.layout.fragment_edit_note),MenuProvider {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note

    private val args: EditNoteFragmentArgs by navArgs()



    // Inflating the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner, Lifecycle.State.RESUMED)

        // Getting reference to the ViewModel shared by MainActivity
        notesViewModel = (activity as MainActivity).noteViewModel
        // Retrieving the current note passed as argument
        currentNote = args.note!!

        // Setting up views with current note data
        binding.editNoteTitle.setText(currentNote.noteTitle)
        binding.editNoteDesc.setText(currentNote.noteDesc)

        // Handling click event on FAB to update note
        binding.editNoteFab.setOnClickListener {
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if (noteTitle.isNotEmpty()){
                // Updating the note in ViewModel
                val note = Note(currentNote.id,noteTitle,noteDesc)
                notesViewModel.updateNote(note)
                // Navigating back to homeFragment
                view.findNavController().popBackStack(R.id.homeFragment,false)
            }else{
                Toast.makeText(context,"Please enter note title",Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to show delete confirmation dialog
    private fun deleteNote(){
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Do you want to delete this note?")
            setPositiveButton("Delete"){_,_ ->
                // Deleting the note from ViewModel
                notesViewModel.deleteNote(currentNote)
                Toast.makeText(context,"Note Deleted",Toast.LENGTH_SHORT).show()
                // Navigating back to homeFragment
                view?.findNavController()?.popBackStack(R.id.homeFragment,false)
            }
            setNegativeButton("Cancel",null)

        }.create().show()
    }

    // Function to create menu in toolbar
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note,menu)
    }

    // Function to handle menu item selection
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.deleteMenu -> {
                deleteNote()
                true
            }
            else -> false
        }
    }

    // Clearing the binding reference when the fragment is destroyed
    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding = null
    }


}


