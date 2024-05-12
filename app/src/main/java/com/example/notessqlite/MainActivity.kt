package com.example.notessqlite

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.notessqlite.database.NoteDatabase
import com.example.notessqlite.repository.NoteRepsitory
import com.example.notessqlite.viewmodel.NoteViewModel
import com.example.notessqlite.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteViewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up ViewModel
        setupViewModel()

    }

    // Function to set up ViewModel
    private fun setupViewModel(){
        // Create a NoteRepository instance
        val noteRepsitory = NoteRepsitory(NoteDatabase(this))
        // Create a ViewModelProviderFactory instance
        val viewModelProviderFactory = NoteViewModelFactory(application,noteRepsitory)
        // Initialize the noteViewModel using ViewModelProvider
        noteViewModel = ViewModelProvider(this,viewModelProviderFactory)[NoteViewModel::class.java]
    }
}