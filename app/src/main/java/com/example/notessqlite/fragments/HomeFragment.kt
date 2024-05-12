package com.example.notessqlite.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView

import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notessqlite.MainActivity
import com.example.notessqlite.R
import com.example.notessqlite.adapter.NoteAdapter
import com.example.notessqlite.databinding.FragmentHomeBinding
import com.example.notessqlite.model.Note
import com.example.notessqlite.viewmodel.NoteViewModel

// Fragment for displaying list of notes
class HomeFragment : Fragment(R.layout.fragment_home),SearchView.OnQueryTextListener,MenuProvider {

    private var homeBinding:FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    // Inflating the layout for this fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adding menu provider for handling toolbar menu
        val menuHost:MenuHost = requireActivity()
        menuHost.addMenuProvider(this,viewLifecycleOwner,Lifecycle.State.RESUMED)

        // Getting reference to the shared ViewModel from MainActivity
        notesViewModel = (activity as MainActivity).noteViewModel

        setupHomeRecycleView() // Ensure this is called before any other function that uses noteAdapter

        // Navigating to addNoteFragment when FAB is clicked
        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    // Function to update UI based on list of notes
    private fun updateUI(note:List<Note>?){
        if (note != null){
            if (note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }

    }

    // Function to set up RecyclerView
    private fun setupHomeRecycleView(){
        noteAdapter = NoteAdapter()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        // Observing changes in notes data and updating RecyclerView accordingly
        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){note ->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            }
        }
    }

    // Function to search notes based on query
    private fun searchNote(query: String?){
        val searchQuery = "%$query"

        notesViewModel.searchNote(searchQuery).observe(this){list ->
            noteAdapter.differ.submitList(list)
        }
    }

    // Callback when user submits query
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    // Callback when text in the SearchView changes
    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText !=null){
            searchNote(newText)
        }
        return true
    }

    // Clearing the binding reference when the fragment is destroyed
    override fun onDestroy() {
        super.onDestroy()
        homeBinding = null
    }

    // Function to create menu in toolbar
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu,menu)

        // Getting reference to SearchView and setting up query listener
        val  menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    // Callback for handling menu item selection
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}